(ns server.core
  (:gen-class)
  (:require [reitit.ring :as ring]
            ;; [reitit.http :as http]
            [reitit.coercion.spec]
            [reitit.swagger :as swagger]
            [reitit.swagger-ui :as swagger-ui]
            [reitit.openapi :as openapi]
            ;; [reitit.http.coercion :as coercion]
            [reitit.ring.coercion :as coercion]
            [reitit.dev.pretty :as pretty]
            ;; [reitit.interceptor.sieppari :as sieppari]

            ;; [reitit.http.interceptors.parameters :as parameters]
            ;; [reitit.http.interceptors.muuntaja :as muuntaja]
            ;; [reitit.http.interceptors.exception :as exception]
            ;; [reitit.http.interceptors.multipart :as multipart]
            [reitit.ring.middleware.parameters :as parameters]
            [reitit.ring.middleware.muuntaja :as muuntaja]
            [reitit.ring.middleware.exception :as exception]
            [reitit.ring.middleware.multipart :as multipart]
            ;; Uncomment to use
            ; [reitit.http.interceptors.dev :as dev]
            ; [reitit.http.spec :as spec]
            ; [spec-tools.spell :as spell]
            [ring.adapter.jetty :as jetty]
            ;; [aleph.http :as aleph]
            [muuntaja.core :as m]
            ;; [clojure.java.io :as io]
            [clojure.spec.alpha :as s]
            ;; untuk generate contoh data
            ;; [clojure.spec.gen.alpha :as gen]
            ;; [spec-tools.core :as st]
            ;; [sieppari.async.manifold]
            ;; [manifold.deferred :as d]
            [server.schema]
            [server.db :as db]))

(def app
  (ring/ring-handler
   (ring/router
    [["/swagger.json"
      {:get {:no-doc true
             :swagger {:info {:title "Kopdar Backoffice API"
                              :description "Kopdar Backoffice build using swagger-docs. TODO:
                              ~~- complete 5 remain tables: `kdstblrprdkdeta`, `kdstblrtokhead`, `kdstbltchathead`, `kdstbltjuadeta`, `kdstbltjuahead`.~~&#x2705;
                              ~~- connection [pooling](https://github.com/bostonaholic/clojure.jdbc-c3p0)~~&#x2705;
                              ~~- apply [redis](https://github.com/taoensso/carmine)~~&#x2705;
                              - apply [auth](https://github.com/metosin/reitit/tree/master/examples/buddy-auth) header
                              - apply dynamic env database switching [environment](https://github.com/weavejester/environ)"
                              :version "0.0.2"}
                       ;; used in /secure APIs below
                       :securityDefinitions {"auth" {:type :apiKey
                                                     :in :header
                                                     :name "Kopdar-Api-Key"}}}
             :handler (swagger/create-swagger-handler)}}]
     ["/openapi.json"
      {:get {:no-doc true
             :openapi {:info {:title "Kopdar Backoffice API"
                              :description "Kopdar Backoffice build using openapi3-docs"
                              :version "0.0.2"}
                       ;; used in /secure APIs below
                       :components {:securitySchemes {"auth" {:type :apiKey
                                                              :in :header
                                                              :name "Kopdar-Api-Key"}}}}
             :handler (openapi/create-openapi-handler)}}]

     ;;;;;;;;;;;;;
     ;; toko
     ;;;;;;;;;;;;
     ["/store"
      {:tags #{"store"}}
      ["/all"
       {:get {:summary "Tabel kstblmasttoko"
              :description "Ambil semua informasi toko"
              :responses {200 {:body :master/stores}}
              :handler (fn [{{{:keys []} :query} :parameters}]
                         {:status 200
                          :body (db/get-all-stores)})}}]
      ["/create"
       {:post {:summary "Insert - Tabel kstblmasttoko"
               :description "Tambah 1 toko"
               :parameters {:body {:store :master/store}}
               :responses {200 {:body {:ok (s/nilable string?)}}}
               :handler (fn [{{{:keys [store]} :body} :parameters}]
                          {:status 200
                           :body {:ok (when (db/create-store store) "success")}})}}]
      ["/update"
       {:put {:summary "Update - Tabel kstblmasttoko"
              :description "Update 1 toko. tbid pada object store harus sama dengan parameter id yang mau di update. Jika beda akan error 500 constraint."
              :parameters {:body {:store :master/store, :id int?}}
              :responses {200 {:body {:ok (s/nilable string?)}}}
              :handler (fn [{{{:keys [store id]} :body} :parameters}]
                         {:status 200
                          :body {:ok (when (db/update-store store id) "success")}})}}]
      ["/delete"
       {:delete {:summary "Hapus - Tabel kstblmasttoko"
                 :description "Hapus 1 toko"
                 :parameters {:query {:id int?}}
                 :responses {200 {:body {:ok (s/nilable string?)}}}
                 :handler (fn [{{{:keys [id]} :query} :parameters}]
                            {:status 200
                             :body {:ok (when (db/delete-store id) "success")}})}}]
      ["/id/:id"
       {:get {:summary "Retrieve 1 toko berdasarkan id"
              :description "Hanya ambil 1 row kdstblmasttoko"
              :parameters {:path {:id int?}}
              :responses {200 {:body :master/store}}
              :handler (fn [{{{:keys [id]} :path} :parameters}]
                         {:status 200
                          :body (db/get-store id)})}}]]
     ;;;;;;;;;;;;;
     ;; etalase
     ;;;;;;;;;;;;
     ["/etalase"
      {:tags #{"etalase"}}
      ["/all"
       {:get {:summary "Tabel kdstblmastetls"
              :description "Ambil semua informasi etalase"
              :responses {200 {:body :master/etalases}}
              :handler (fn [{{{:keys []} :query} :parameters}]
                         {:status 200
                          :body (db/get-all-etalases)})}}]
      ["/create"
       {:post {:summary "Insert - Tabel kdstblmastetls"
               :description "Tambah 1 etalase"
               :parameters {:body {:etalase :master/etalase}}
               :responses {200 {:body {:ok (s/nilable string?)}}}
               :handler (fn [{{{:keys [etalase]} :body} :parameters}]
                          {:status 200
                           :body {:ok (when (db/create-etalase etalase) "success")}})}}]
      ["/update"
       {:put {:summary "Update - Tabel kdstblmastetls berdasarkan tbid"
              :description "Update 1 etalase. tbid pada object etalase harus sama dengan parameter id yang mau di update. Jika beda akan error 500 constraint."
              :parameters {:body {:etalase :master/etalase, :id int?}}
              :responses {200 {:body {:ok (s/nilable string?)}}}
              :handler (fn [{{{:keys [etalase id]} :body} :parameters}]
                         {:status 200
                          :body {:ok (when (db/update-etalase etalase id) "success")}})}}]
      ["/delete"
       {:delete {:summary "Hapus - Tabel kdstblmastetls berdasarkan tbid"
                 :description "Hapus 1 etalase"
                 :parameters {:query {:id int?}}
                 :responses {200 {:body {:ok (s/nilable string?)}}}
                 :handler (fn [{{{:keys [id]} :query} :parameters}]
                            {:status 200
                             :body {:ok (when (db/delete-etalase id) "success")}})}}]
      ["/id/:id"
       {:get {:summary "Retrieve 1 etalase berdasarkan tbid"
              :description "Hanya ambil 1 row kdstblmastetls"
              :parameters {:path {:id int?}}
              :responses {200 {:body :master/etalase}}
              :handler (fn [{{{:keys [id]} :path} :parameters}]
                         {:status 200
                          :body (db/get-etalase id)})}}]]

     ;;;;;;;;;;;;;
     ;; category1
     ;;;;;;;;;;;;
     ["/category1"
      {:tags #{"category-one"}}
      ["/all"
       {:get {:summary "Tabel `kdstblmastkat1`"
              :description "Ambil semua informasi kategori pertama"
              :responses {200 {:body :master/category-ones}}
              :handler (fn [{{{:keys []} :query} :parameters}]
                         {:status 200
                          :body (db/get-all-category-one)})}}]
      ["/create"
       {:post {:summary "Insert - Tabel `kdstblmastkat1`"
               :description "Tambah 1 record kategori pertama"
               :parameters {:body {:category :master/category-one}}
               :responses {200 {:body {:ok (s/nilable string?)}}}
               :handler (fn [{{{:keys [category]} :body} :parameters}]
                          {:status 200
                           :body {:ok (when (db/create-category-one category) "success")}})}}]
      ["/update"
       {:put {:summary "Update - Tabel `kdstblmastkat1` berdasarkan `tbid`"
              :description "Update 1 record kategori pertama. `tbid` pada object kategori pertama harus sama dengan parameter `id` yang mau di update. Jika beda akan error 500 constraint."
              :parameters {:body {:category :master/category-one, :id int?}}
              :responses {200 {:body {:ok (s/nilable string?)}}}
              :handler (fn [{{{:keys [category id]} :body} :parameters}]
                         {:status 200
                          :body {:ok (when (db/update-category-one category id) "success")}})}}]
      ["/delete"
       {:delete {:summary "Hapus - Tabel `kdstblmastkat1` berdasarkan `tbid`"
                 :description "Hapus 1 record kategori pertama"
                 :parameters {:query {:id int?}}
                 :responses {200 {:body {:ok (s/nilable string?)}}}
                 :handler (fn [{{{:keys [id]} :query} :parameters}]
                            {:status 200
                             :body {:ok (when (db/delete-category-one id) "success")}})}}]
      ["/id/:id"
       {:get {:summary "Retrieve 1 etalase berdasarkan tbid"
              :description "Hanya ambil 1 row `kdstblmastkat1`"
              :parameters {:path {:id int?}}
              :responses {200 {:body :master/category-one}}
              :handler (fn [{{{:keys [id]} :path} :parameters}]
                         {:status 200
                          :body (db/get-category-one id)})}}]]

     ;;;;;;;;;;;;;
     ;; category2
     ;;;;;;;;;;;;
     ["/category2"
      {:tags #{"category-one"}}
      ["/all"
       {:get {:summary "Tabel kdstblmastkat2"
              :description "Ambil semua informasi kategori kedua"
              :responses {200 {:body :master/category-twos}}
              :handler (fn [{{{:keys []} :query} :parameters}]
                         {:status 200
                          :body (db/get-all-category-two)})}}]
      ["/create"
       {:post {:summary "Insert - Tabel `kdstblmastkat2`"
               :description "Tambah 1 record kategori kedua"
               :parameters {:body {:category :master/category-two}}
               :responses {200 {:body {:ok (s/nilable string?)}}}
               :handler (fn [{{{:keys [category]} :body} :parameters}]
                          {:status 200
                           :body {:ok (when (db/create-category-two category) "success")}})}}]
      ["/update"
       {:put {:summary "Update - Tabel `kdstblmastkat2` berdasarkan `tbid`"
              :description "Update 1 record kategori kedua. `tbid` pada object kategori harus sama dengan parameter `id` yang mau di update. Jika beda akan error 500 constraint."
              :parameters {:body {:category :master/category-two, :id int?}}
              :responses {200 {:body {:ok (s/nilable string?)}}}
              :handler (fn [{{{:keys [category id]} :body} :parameters}]
                         {:status 200
                          :body {:ok (when (db/update-category-two category id) "success")}})}}]
      ["/delete"
       {:delete {:summary "Hapus - Tabel `kdstblmastkat2` berdasarkan `tbid`"
                 :description "Hapus 1 record kategori kedua"
                 :parameters {:query {:id int?}}
                 :responses {200 {:body {:ok (s/nilable string?)}}}
                 :handler (fn [{{{:keys [id]} :query} :parameters}]
                            {:status 200
                             :body {:ok (when (db/delete-category-two id) "success")}})}}]
      ["/id/:id"
       {:get {:summary "Retrieve 1 record kategory ketiga berdasarkan `tbid`"
              :description "Hanya ambil 1 row `kdstblmastkat3`"
              :parameters {:path {:id int?}}
              :responses {200 {:body :master/category-three}}
              :handler (fn [{{{:keys [id]} :path} :parameters}]
                         {:status 200
                          :body (db/get-category-two id)})}}]]

     ;;;;;;;;;;;;;
     ;; category3
     ;;;;;;;;;;;;
     ["/category3"
      {:tags #{"category-three"}}
      ["/all"
       {:get {:summary "Tabel kdstblmastkat3"
              :description "Ambil semua informasi kategori ketiga"
              :responses {200 {:body :master/category-threes}}
              :handler (fn [{{{:keys []} :query} :parameters}]
                         {:status 200
                          :body (db/get-all-category-three)})}}]
      ["/create"
       {:post {:summary "Insert - Tabel `kdstblmastkat3`"
               :description "Tambah 1 record kategori ketiga"
               :parameters {:body {:category :master/category-three}}
               :responses {200 {:body {:ok (s/nilable string?)}}}
               :handler (fn [{{{:keys [category]} :body} :parameters}]
                          {:status 200
                           :body {:ok (when (db/create-category-three category) "success")}})}}]
      ["/update"
       {:put {:summary "Update - Tabel `kdstblmastkat3` berdasarkan `tbid`"
              :description "Update 1 record kategori ketiga. `tbid` pada object kategori harus sama dengan parameter `id` yang mau di update. Jika beda akan error 500 constraint."
              :parameters {:body {:category :master/category-three, :id int?}}
              :responses {200 {:body {:ok (s/nilable string?)}}}
              :handler (fn [{{{:keys [category id]} :body} :parameters}]
                         {:status 200
                          :body {:ok (when (db/update-category-three category id) "success")}})}}]
      ["/delete"
       {:delete {:summary "Hapus - Tabel `kdstblmastkat3` berdasarkan `tbid`"
                 :description "Hapus 1 record kategori ketiga"
                 :parameters {:query {:id int?}}
                 :responses {200 {:body {:ok (s/nilable string?)}}}
                 :handler (fn [{{{:keys [id]} :query} :parameters}]
                            {:status 200
                             :body {:ok (when (db/delete-category-three id) "success")}})}}]
      ["/id/:id"
       {:get {:summary "Retrieve 1 record kategory ketiga berdasarkan `tbid`"
              :description "Hanya ambil 1 row `kdstblmastkat3`"
              :parameters {:path {:id int?}}
              :responses {200 {:body :master/category-three}}
              :handler (fn [{{{:keys [id]} :path} :parameters}]
                         {:status 200
                          :body (db/get-category-three id)})}}]]

     ;;;;;;;;;;;;;
     ;; alamat
     ;;;;;;;;;;;;
     ["/user-address"
      {:tags #{"user-address"}}
      ["/all"
       {:get {:summary "Tabel kdstblalmtuser"
              :description "Ambil semua informasi alamat user"
              :responses {200 {:body :master/user-addresses}}
              :handler (fn [{{{:keys []} :query} :parameters}]
                         {:status 200
                          :body (db/get-all-user-addresses)})}}]
      ["/create"
       {:post {:summary "Insert - Tabel `kdstblalmtuser`"
               :description "Tambah 1 record alamat user"
               :parameters {:body {:address :master/user-address}}
               :responses {200 {:body {:ok (s/nilable string?)}}}
               :handler (fn [{{{:keys [address]} :body} :parameters}]
                          {:status 200
                           :body {:ok (when (db/create-user-address address) "success")}})}}]
      ["/update"
       {:put {:summary "Update - Tabel `kdstblalmtuser` berdasarkan `tbid`"
              :description "Update 1 record alamat user. `tbid` pada object address harus sama dengan parameter `id` yang mau di update. Jika beda akan error 500 constraint."
              :parameters {:body {:address :master/user-address, :id int?}}
              :responses {200 {:body {:ok (s/nilable string?)}}}
              :handler (fn [{{{:keys [address id]} :body} :parameters}]
                         {:status 200
                          :body {:ok (when (db/update-user-address address id) "success")}})}}]
      ["/delete"
       {:delete {:summary "Hapus - Tabel `kdstblalmtuser` berdasarkan `tbid`"
                 :description "Hapus 1 record alamat user"
                 :parameters {:query {:id int?}}
                 :responses {200 {:body {:ok (s/nilable string?)}}}
                 :handler (fn [{{{:keys [id]} :query} :parameters}]
                            {:status 200
                             :body {:ok (when (db/delete-user-address id) "success")}})}}]
      ["/id/:id"
       {:get {:summary "Retrieve 1 record kategory ketiga berdasarkan `tbid`"
              :description "Hanya ambil 1 row `kdstblalmtuser`"
              :parameters {:path {:id int?}}
              :responses {200 {:body :master/user-address}}
              :handler (fn [{{{:keys [id]} :path} :parameters}]
                         {:status 200
                          :body (db/get-user-address id)})}}]]
     ;;;;;;;;;;;;;
     ;; metode bayar
     ;;;;;;;;;;;;
     ["/payment-method"
      {:tags #{"payment-method"}}
      ["/all"
       {:get {:summary "Tabel `kdstblmastmethbayr`"
              :description "Ambil semua informasi metode pembayaran"
              :responses {200 {:body :master/payment-methods}}
              :handler (fn [{{{:keys []} :query} :parameters}]
                         {:status 200
                          :body (db/get-all-payment-methods)})}}]
      ["/create"
       {:post {:summary "Insert - Tabel `kdstblmastmethbayr`"
               :description "Tambah 1 record metode bayar"
               :parameters {:body {:method :master/payment-method}}
               :responses {200 {:body {:ok (s/nilable string?)}}}
               :handler (fn [{{{:keys [method]} :body} :parameters}]
                          {:status 200
                           :body {:ok (when (db/create-payment-method method) "success")}})}}]
      ["/update"
       {:put {:summary "Update - Tabel `kdstblmastmethbayr` berdasarkan `tbid`"
              :description "Update 1 record metode bayar. `tbid` pada object `method` harus sama dengan parameter `id` yang mau di update. Jika beda akan error 500 constraint."
              :parameters {:body {:method :master/payment-method, :id int?}}
              :responses {200 {:body {:ok (s/nilable string?)}}}
              :handler (fn [{{{:keys [method id]} :body} :parameters}]
                         {:status 200
                          :body {:ok (when (db/update-payment-method method id) "success")}})}}]
      ["/delete"
       {:delete {:summary "Hapus - Tabel `kdstblmastmethbayr` berdasarkan `tbid`"
                 :description "Hapus 1 record metode bayar"
                 :parameters {:query {:id int?}}
                 :responses {200 {:body {:ok (s/nilable string?)}}}
                 :handler (fn [{{{:keys [id]} :query} :parameters}]
                            {:status 200
                             :body {:ok (when (db/delete-payment-method id) "success")}})}}]
      ["/id/:id"
       {:get {:summary "Retrieve 1 record metode pembayaran berdasarkan `tbid`"
              :description "Hanya ambil 1 row `kdstblmastmethbayr`"
              :parameters {:path {:id int?}}
              :responses {200 {:body :master/payment-method}}
              :handler (fn [{{{:keys [id]} :path} :parameters}]
                         {:status 200
                          :body (db/get-payment-method id)})}}]]

     ;;;;;;;;;;;;;
     ;; pengiriman
     ;;;;;;;;;;;;
     ["/delivery"
      {:tags #{"courier-delivery"}}
      ["/all"
       {:get {:summary "Tabel `kdstblmastpengiriman`"
              :description "Ambil semua informasi pengiriman"
              :responses {200 {:body :master/deliveries}}
              :handler (fn [{{{:keys []} :query} :parameters}]
                         {:status 200
                          :body (db/get-all-deliveries)})}}]
      ["/create"
       {:post {:summary "Insert - Tabel `kdstblmastpengiriman`"
               :description "Tambah 1 record pengiriman"
               :parameters {:body {:delivery :master/delivery}}
               :responses {200 {:body {:ok (s/nilable string?)}}}
               :handler (fn [{{{:keys [delivery]} :body} :parameters}]
                          {:status 200
                           :body {:ok (when (db/create-delivery delivery) "success")}})}}]
      ["/update"
       {:put {:summary "Update - Tabel `kdstblmastpengiriman` berdasarkan `tbid`"
              :description "Update 1 record pengiriman. `tbid` pada object `delivery` harus sama dengan parameter `id` yang mau di update. Jika beda akan error 500 constraint."
              :parameters {:body {:delivery :master/delivery, :id int?}}
              :responses {200 {:body {:ok (s/nilable string?)}}}
              :handler (fn [{{{:keys [delivery id]} :body} :parameters}]
                         {:status 200
                          :body {:ok (when (db/update-delivery delivery id) "success")}})}}]
      ["/delete"
       {:delete {:summary "Hapus - Tabel `kdstblmastpengiriman` berdasarkan `tbid`"
                 :description "Hapus 1 record pengiriman"
                 :parameters {:query {:id int?}}
                 :responses {200 {:body {:ok (s/nilable string?)}}}
                 :handler (fn [{{{:keys [id]} :query} :parameters}]
                            {:status 200
                             :body {:ok (when (db/delete-delivery id) "success")}})}}]
      ["/id/:id"
       {:get {:summary "Retrieve 1 record pengiriman berdasarkan `tbid`"
              :description "Hanya ambil 1 row `kdstblmastpengiriman`"
              :parameters {:path {:id int?}}
              :responses {200 {:body :master/delivery}}
              :handler (fn [{{{:keys [id]} :path} :parameters}]
                         {:status 200
                          :body (db/get-delivery id)})}}]]

     ;;;;;;;;;;;;;
     ;; status transaksi
     ;;;;;;;;;;;;
     ["/trx-status"
      {:tags #{"transaction-status"}}
      ["/all"
       {:get {:summary "Tabel `kdstblmasttranstat`"
              :description "Ambil semua informasi status transaksi"
              :responses {200 {:body :master/trx-statuses}}
              :handler (fn [{{{:keys []} :query} :parameters}]
                         {:status 200
                          :body (db/get-all-trx-statuses)})}}]
      ["/create"
       {:post {:summary "Insert - Tabel `kdstblmasttranstat`"
               :description "Tambah 1 record status transaksi"
               :parameters {:body {:trxstatus :master/trx-status}}
               :responses {200 {:body {:ok (s/nilable string?)}}}
               :handler (fn [{{{:keys [trxstatus]} :body} :parameters}]
                          {:status 200
                           :body {:ok (when (db/create-trx-status trxstatus) "success")}})}}]
      ["/update"
       {:put {:summary "Update - Tabel `kdstblmasttranstat` berdasarkan `tbid`"
              :description "Update 1 record status transaksi. `tbid` pada object `status` harus sama dengan parameter `id` yang mau di update. Jika beda akan error 500 constraint."
              :parameters {:body {:trxstatus :master/trx-status, :id int?}}
              :responses {200 {:body {:ok (s/nilable string?)}}}
              :handler (fn [{{{:keys [trxstatus id]} :body} :parameters}]
                         {:status 200
                          :body {:ok (when (db/update-trx-status trxstatus id) "success")}})}}]
      ["/delete"
       {:delete {:summary "Hapus - Tabel `kdstblmasttranstat` berdasarkan `tbid`"
                 :description "Hapus 1 record status transaksi"
                 :parameters {:query {:id int?}}
                 :responses {200 {:body {:ok (s/nilable string?)}}}
                 :handler (fn [{{{:keys [id]} :query} :parameters}]
                            {:status 200
                             :body {:ok (when (db/delete-trx-status id) "success")}})}}]
      ["/id/:id"
       {:get {:summary "Retrieve 1 record status transaksi berdasarkan `tbid`"
              :description "Hanya ambil 1 row `kdstblmasttranstat`"
              :parameters {:path {:id int?}}
              :responses {200 {:body :master/trx-status}}
              :handler (fn [{{{:keys [id]} :path} :parameters}]
                         {:status 200
                          :body (db/get-trx-status id)})}}]]

     ;;;;;;;;;;;;;
     ;; user group
     ;;;;;;;;;;;;
     ["/user-group"
      {:tags #{"user-group"}}
      ["/all"
       {:get {:summary "Tabel `kdstblmastugrp`"
              :description "Ambil semua informasi group"
              :responses {200 {:body :master/user-groups}}
              :handler (fn [{{{:keys []} :query} :parameters}]
                         {:status 200
                          :body (db/get-all-user-groups)})}}]
      ["/create"
       {:post {:summary "Insert - Tabel `kdstblmastugrp`"
               :description "Tambah 1 record status transaksi"
               :parameters {:body {:group :master/user-group}}
               :responses {200 {:body {:ok (s/nilable string?)}}}
               :handler (fn [{{{:keys [group]} :body} :parameters}]
                          {:status 200
                           :body {:ok (when (db/create-user-group group) "success")}})}}]
      ["/update"
       {:put {:summary "Update - Tabel `kdstblmastugrp` berdasarkan `tbid`"
              :description "Update 1 record group. `tbid` pada object `status` harus sama dengan parameter `id` yang mau di update. Jika beda akan error 500 constraint."
              :parameters {:body {:group :master/user-group, :id int?}}
              :responses {200 {:body {:ok (s/nilable string?)}}}
              :handler (fn [{{{:keys [group id]} :body} :parameters}]
                         {:status 200
                          :body {:ok (when (db/update-user-group group id) "success")}})}}]
      ["/delete"
       {:delete {:summary "Hapus - Tabel `kdstblmastugrp` berdasarkan `tbid`"
                 :description "Hapus 1 record group"
                 :parameters {:query {:id int?}}
                 :responses {200 {:body {:ok (s/nilable string?)}}}
                 :handler (fn [{{{:keys [id]} :query} :parameters}]
                            {:status 200
                             :body {:ok (when (db/delete-user-group id) "success")}})}}]
      ["/id/:id"
       {:get {:summary "Retrieve 1 record group berdasarkan `tbid`"
              :description "Hanya ambil 1 row `kdstblmastugrp`"
              :parameters {:path {:id int?}}
              :responses {200 {:body :master/user-group}}
              :handler (fn [{{{:keys [id]} :path} :parameters}]
                         {:status 200
                          :body (db/get-user-group id)})}}]]

     ;;;;;;;;;;;;;
     ;; user
     ;;;;;;;;;;;;
     ["/user"
      {:tags #{"apps-user"}}
      ["/all"
       {:get {:summary "Tabel `kdstblmastuser`"
              :description "Ambil semua informasi user"
              :responses {200 {:body :master/users}}
              :handler (fn [{{{:keys []} :query} :parameters}]
                         {:status 200
                          :body (db/get-all-users)})}}]
      ["/create"
       {:post {:summary "Insert - Tabel `kdstblmastuser`"
               :description "Tambah 1 record user"
               :parameters {:body {:user :master/user}}
               :responses {200 {:body {:ok (s/nilable string?)}}}
               :handler (fn [{{{:keys [user]} :body} :parameters}]
                          {:status 200
                           :body {:ok (when (db/create-user user) "success")}})}}]
      ["/update"
       {:put {:summary "Update - Tabel `kdstblmastuser` berdasarkan `tbid`"
              :description "Update 1 record user. `tbid` pada object `status` harus sama dengan parameter `id` yang akan di update. Jika beda akan error 500 constraint."
              :parameters {:body {:user :master/user, :id int?}}
              :responses {200 {:body {:ok (s/nilable string?)}}}
              :handler (fn [{{{:keys [user id]} :body} :parameters}]
                         {:status 200
                          :body {:ok (when (db/update-user user id) "success")}})}}]
      ["/delete"
       {:delete {:summary "Hapus - Tabel `kdstblmastuser` berdasarkan `tbid`"
                 :description "Hapus 1 record user"
                 :parameters {:query {:id int?}}
                 :responses {200 {:body {:ok (s/nilable string?)}}}
                 :handler (fn [{{{:keys [id]} :query} :parameters}]
                            {:status 200
                             :body {:ok (when (db/delete-user id) "success")}})}}]
      ["/id/:id"
       {:get {:summary "Retrieve 1 record user berdasarkan `tbid`"
              :description "Hanya ambil 1 row `kdstblmastuser`"
              :parameters {:path {:id int?}}
              :responses {200 {:body :master/user}}
              :handler (fn [{{{:keys [id]} :path} :parameters}]
                         {:status 200
                          :body (db/get-user id)})}}]]

     ;;;;;;;;;;;;;
     ;; rate product
     ;;;;;;;;;;;;
     ["/rate-product"
      {:tags #{"rate-product-detail"}}
      ["/all"
       {:get {:summary "Tabel `kdstblrprdkdeta`"
              :description "Ambil semua informasi detail produk"
              :responses {200 {:body :rate/product-details}}
              :handler (fn [{{{:keys []} :query} :parameters}]
                         {:status 200
                          :body (db/get-all-product-details)})}}]
      ["/create"
       {:post {:summary "Insert - Tabel `kdstblrprdkdeta`"
               :description "Tambah 1 record detail produk"
               :parameters {:body {:detail :rate/product-detail}}
               :responses {200 {:body {:ok (s/nilable string?)}}}
               :handler (fn [{{{:keys [detail]} :body} :parameters}]
                          {:status 200
                           :body {:ok (when (db/create-product-detail detail) "success")}})}}]
      ["/update"
       {:put {:summary "Update - Tabel `kdstblrprdkdeta` berdasarkan `tbid`"
              :description "Update 1 record rating detail produk. `tbid` pada object `detail` harus sama dengan parameter `id` yang akan di update. Jika beda akan error 500 constraint."
              :parameters {:body {:detail :rate/product-detail, :id int?}}
              :responses {200 {:body {:ok (s/nilable string?)}}}
              :handler (fn [{{{:keys [detail id]} :body} :parameters}]
                         {:status 200
                          :body {:ok (when (db/update-product-detail detail id) "success")}})}}]
      ["/delete"
       {:delete {:summary "Hapus - Tabel `kdstblrprdkdeta` berdasarkan `tbid`"
                 :description "Hapus 1 record rating detail produk"
                 :parameters {:query {:id int?}}
                 :responses {200 {:body {:ok (s/nilable string?)}}}
                 :handler (fn [{{{:keys [id]} :query} :parameters}]
                            {:status 200
                             :body {:ok (when (db/delete-product-detail id) "success")}})}}]
      ["/id/:id"
       {:get {:summary "Retrieve 1 record rekap detail produk berdasarkan `tbid`"
              :description "Hanya ambil 1 row `kdstblrprdkdeta`"
              :parameters {:path {:id int?}}
              :responses {200 {:body :rate/product-detail}}
              :handler (fn [{{{:keys [id]} :path} :parameters}]
                         {:status 200
                          :body (db/get-product-detail id)})}}]]

     ;;;;;;;;;;;;;
     ;; rate header toko
     ;;;;;;;;;;;;
     ["/rate-store"
      {:tags #{"rate-store-header"}}
      ["/all"
       {:get {:summary "Tabel `kdstblrtokhead`"
              :description "Ambil semua informasi rating toko"
              :responses {200 {:body :rate/store-headers}}
              :handler (fn [{{{:keys []} :query} :parameters}]
                         {:status 200
                          :body (db/get-all-store-headers)})}}]
      ["/create"
       {:post {:summary "Insert - Tabel `kdstblrtokhead`"
               :description "Tambah 1 record rating toko"
               :parameters {:body {:header :rate/store-header}}
               :responses {200 {:body {:ok (s/nilable string?)}}}
               :handler (fn [{{{:keys [header]} :body} :parameters}]
                          {:status 200
                           :body {:ok (when (db/create-store-header header) "success")}})}}]
      ["/update"
       {:put {:summary "Update - Tabel `kdstblrtokhead` berdasarkan `tbid`"
              :description "Update 1 record rate toko. `tbid` pada object `header` harus sama dengan parameter `id` yang akan di update. Jika beda akan error 500 constraint."
              :parameters {:body {:header :rate/store-header, :id int?}}
              :responses {200 {:body {:ok (s/nilable string?)}}}
              :handler (fn [{{{:keys [header id]} :body} :parameters}]
                         {:status 200
                          :body {:ok (when (db/update-store-header header id) "success")}})}}]
      ["/delete"
       {:delete {:summary "Hapus - Tabel `kdstblrtokhead` berdasarkan `tbid`"
                 :description "Hapus 1 record rate header toko"
                 :parameters {:query {:id int?}}
                 :responses {200 {:body {:ok (s/nilable string?)}}}
                 :handler (fn [{{{:keys [id]} :query} :parameters}]
                            {:status 200
                             :body {:ok (when (db/delete-store-header id) "success")}})}}]
      ["/id/:id"
       {:get {:summary "Retrieve 1 record rate header toko berdasarkan `tbid`"
              :description "Hanya ambil 1 row `kdstblrtokhead`"
              :parameters {:path {:id int?}}
              :responses {200 {:body :rate/store-header}}
              :handler (fn [{{{:keys [id]} :path} :parameters}]
                         {:status 200
                          :body (db/get-store-header id)})}}]]

     ;;;;;;;;;;;;;
     ;; header chat/obrolan
     ;;;;;;;;;;;;
     ["/chat-header"
      {:tags #{"chat-header"}}
      ["/all"
       {:get {:summary "Tabel `kdstbltchathead`"
              :description "Ambil semua informasi header chat"
              :responses {200 {:body :trx/chat-headers}}
              :handler (fn [{{{:keys []} :query} :parameters}]
                         {:status 200
                          :body (db/get-all-chat-headers)})}}]
      ["/create"
       {:post {:summary "Insert - Tabel `kdstbltchathead`"
               :description "Tambah 1 record header chat"
               :parameters {:body {:header :trx/chat-header}}
               :responses {200 {:body {:ok (s/nilable string?)}}}
               :handler (fn [{{{:keys [header]} :body} :parameters}]
                          {:status 200
                           :body {:ok (when (db/create-chat-header header) "success")}})}}]
      ["/update"
       {:put {:summary "Update - Tabel `kdstbltchathead` berdasarkan `tbid`"
              :description "Update 1 record chat header. `tbid` pada object `header` harus sama dengan parameter `id` yang akan di update. Jika beda akan error 500 constraint."
              :parameters {:body {:header :trx/chat-header, :id int?}}
              :responses {200 {:body {:ok (s/nilable string?)}}}
              :handler (fn [{{{:keys [header id]} :body} :parameters}]
                         {:status 200
                          :body {:ok (when (db/update-chat-header header id) "success")}})}}]
      ["/delete"
       {:delete {:summary "Hapus - Tabel `kdstbltchathead` berdasarkan `tbid`"
                 :description "Hapus 1 record detail header chat"
                 :parameters {:query {:id int?}}
                 :responses {200 {:body {:ok (s/nilable string?)}}}
                 :handler (fn [{{{:keys [id]} :query} :parameters}]
                            {:status 200
                             :body {:ok (when (db/delete-chat-header id) "success")}})}}]
      ["/id/:id"
       {:get {:summary "Retrieve 1 record header chat berdasarkan `tbid`"
              :description "Hanya ambil 1 row `kdstbltchathead`"
              :parameters {:path {:id int?}}
              :responses {200 {:body :trx/chat-header}}
              :handler (fn [{{{:keys [id]} :path} :parameters}]
                         {:status 200
                          :body (db/get-chat-header id)})}}]]

     ;;;;;;;;;;;;;
     ;; detail transaksi jual
     ;;;;;;;;;;;;
     ["/trx-detail"
      {:tags #{"trx-detail"}}
      ["/all"
       {:get {:summary "Tabel `kdstbltjuadeta`"
              :description "Ambil semua informasi detail transaksi jual"
              :responses {200 {:body :trx/selling-details}}
              :handler (fn [{{{:keys []} :query} :parameters}]
                         {:status 200
                          :body (db/get-all-trx-details)})}}]
      ["/create"
       {:post {:summary "Insert - Tabel `kdstbltjuadeta`"
               :description "Tambah 1 record detail transaksi jual"
               :parameters {:body {:detail :trx/selling-detail}}
               :responses {200 {:body {:ok (s/nilable string?)}}}
               :handler (fn [{{{:keys [detail]} :body} :parameters}]
                          {:status 200
                           :body {:ok (when (db/create-trx-detail detail) "success")}})}}]
      ["/update"
       {:put {:summary "Update - Tabel `kdstbltjuadeta` berdasarkan `tbid`"
              :description "Update 1 record detail transaksi jual. `tbid` pada object `detail` harus sama dengan parameter `id` yang akan di update. Jika beda akan error 500 constraint."
              :parameters {:body {:detail :trx/selling-detail, :id int?}}
              :responses {200 {:body {:ok (s/nilable string?)}}}
              :handler (fn [{{{:keys [detail id]} :body} :parameters}]
                         {:status 200
                          :body {:ok (when (db/update-trx-detail detail id) "success")}})}}]
      ["/delete"
       {:delete {:summary "Hapus - Tabel `kdstbltjuadeta` berdasarkan `tbid`"
                 :description "Hapus 1 record detail transaksi jual"
                 :parameters {:query {:id int?}}
                 :responses {200 {:body {:ok (s/nilable string?)}}}
                 :handler (fn [{{{:keys [id]} :query} :parameters}]
                            {:status 200
                             :body {:ok (when (db/delete-trx-detail id) "success")}})}}]
      ["/id/:id"
       {:get {:summary "Retrieve 1 record detail transaksi jual berdasarkan `tbid`"
              :description "Hanya ambil 1 row `kdstbltjuadeta`"
              :parameters {:path {:id int?}}
              :responses {200 {:body :trx/selling-detail}}
              :handler (fn [{{{:keys [id]} :path} :parameters}]
                         {:status 200
                          :body (db/get-trx-detail id)})}}]]

     ;;;;;;;;;;;;;
     ;; header transaksi jual
     ;;;;;;;;;;;;
     ["/trx-header"
      {:tags #{"trx-header"}}
      ["/all"
       {:get {:summary "Tabel `kdstbltjuahead`"
              :description "Ambil semua informasi header transaksi jual"
              :responses {200 {:body :trx/selling-headers}}
              :handler (fn [{{{:keys []} :query} :parameters}]
                         {:status 200
                          :body (db/get-all-trx-headers)})}}]
      ["/create"
       {:post {:summary "Insert - Tabel `kdstbltjuahead`"
               :description "Tambah 1 record header transaksi jual"
               :parameters {:body {:header :trx/selling-header}}
               :responses {200 {:body {:ok (s/nilable string?)}}}
               :handler (fn [{{{:keys [header]} :body} :parameters}]
                          {:status 200
                           :body {:ok (when (db/create-trx-header header) "success")}})}}]
      ["/update"
       {:put {:summary "Update - Tabel `kdstbltjuahead` berdasarkan `tbid`"
              :description "Update 1 record header transaksi jual. `tbid` pada object `header` harus sama dengan parameter `id` yang akan di update. Jika beda akan error 500 constraint."
              :parameters {:body {:header :trx/selling-header, :id int?}}
              :responses {200 {:body {:ok (s/nilable string?)}}}
              :handler (fn [{{{:keys [header id]} :body} :parameters}]
                         {:status 200
                          :body {:ok (when (db/update-trx-header header id) "success")}})}}]
      ["/delete"
       {:delete {:summary "Hapus - Tabel `kdstbltjuahead` berdasarkan `tbid`"
                 :description "Hapus 1 record header transaksi jual"
                 :parameters {:query {:id int?}}
                 :responses {200 {:body {:ok (s/nilable string?)}}}
                 :handler (fn [{{{:keys [id]} :query} :parameters}]
                            {:status 200
                             :body {:ok (when (db/delete-trx-header id) "success")}})}}]
      ["/id/:id"
       {:get {:summary "Retrieve 1 record header transaksi jual berdasarkan `tbid`"
              :description "Hanya ambil 1 row `kdstbltjuahead`"
              :parameters {:path {:id int?}}
              :responses {200 {:body :trx/selling-header}}
              :handler (fn [{{{:keys [id]} :path} :parameters}]
                         {:status 200
                          :body (db/get-trx-header id)})}}]]

     ;;;;;;;;;;;;;
     ;; product
     ;;;;;;;;;;;;
     ["/product"
      {:tags #{"product"}}
      ["/all"
       {:get {:summary "Tabel `kdstblmastprdk`"
              :description "Ambil semua informasi produk"
              :responses {200 {:body :master/products}}
              :handler (fn [{{{:keys []} :query} :parameters}]
                         {:status 200
                          :body (db/get-all-products)})}}]
      ["/create"
       {:post {:summary "Insert - Tabel `kdstblmastprdk`"
               :description "Tambah 1 produk"
               :parameters {:body {:etalase :master/product}}
               :responses {200 {:body {:ok (s/nilable string?)}}}
               :handler (fn [{{{:keys [product]} :body} :parameters}]
                          {:status 200
                           :body {:ok (when (db/create-product product) "success")}})}}]
      ["/update"
       {:put {:summary "Update - Tabel `kdstblmastprdk` berdasarkan `tbid`"
              :description "Update 1 produk. `tbid` pada object `product` harus sama dengan parameter `id` yang mau di update. Jika beda akan error 500 constraint."
              :parameters {:body {:product :master/product, :id int?}}
              :responses {200 {:body {:ok (s/nilable string?)}}}
              :handler (fn [{{{:keys [product id]} :body} :parameters}]
                         {:status 200
                          :body {:ok (when (db/update-product product id) "success")}})}}]
      ["/delete"
       {:delete {:summary "Hapus - Tabel `kdstblmastprdk` berdasarkan `tbid`"
                 :description "Hapus 1 produk"
                 :parameters {:query {:id int?}}
                 :responses {200 {:body {:ok (s/nilable string?)}}}
                 :handler (fn [{{{:keys [id]} :query} :parameters}]
                            {:status 200
                             :body {:ok (when (db/delete-product id) "success")}})}}]
      ["/id/:id"
       {:get {:summary "Retrieve 1 produk berdasarkan `tbid`"
              :description "Hanya ambil 1 row `kdstblmastprdk`"
              :parameters {:path {:id int?}}
              :responses {200 {:body :master/product}}
              :handler (fn [{{{:keys [id]} :path} :parameters}]
                         {:status 200
                          :body (db/get-product id)})}}]
      ["/images/:id"
       {:get {:summary "Retrieve 1 produk image, rating, flag favorite atau populer berdasarkan `tbid`"
              :description "Array images, rating dan populer dari table `kdstblmastprdkdeta_` dan `kdstblmastprdkimag_`"
              :parameters {:path {:id int?}}
              :responses {200 {:body :master/product-images-rating}}
              :handler (fn [{{{:keys [id]} :path} :parameters}]
                         {:status 200
                          :body (let [prating (db/get-product-rating id)
                                      pimages (db/get-product-images-arr id)] ;; image in array format
                                  {:tokotbid id :images pimages :rating prating})})}}]
      ]

     ["/secure"
      {:tags #{"secure"}
       :openapi {:security [{"auth" []}]}
       :swagger {:security [{"auth" []}]}}
      ["/get"
       {:get {:summary "endpoint authenticated with a header"
              :responses {200 {:body {:secret string?}}
                          401 {:body {:error string?}}}
              :handler (fn [request]
                         ;; In a real app authentication would be handled by middleware
                         (if (= "satset" (get-in request [:headers "kopdar-api-key"]))
                           {:status 200
                            :body {:secret "this request is authorized"}}
                           {:status 401
                            :body {:error "unauthorized"}}))}}]]]

    {;:reitit.interceptor/transform dev/print-context-diffs ;; pretty context diffs
     ;;:validate spec/validate ;; enable spec validation for route data
     ;;:reitit.spec/wrap spell/closed ;; strict top-level validation
     :exception pretty/exception
     :data {:coercion reitit.coercion.spec/coercion
            :muuntaja m/instance
            :middleware [;; swagger feature
                         swagger/swagger-feature
                           ;; openapi feature
                         openapi/openapi-feature
                           ;; query-params & form-params
                         parameters/parameters-middleware
                           ;; content-negotiation
                         muuntaja/format-negotiate-middleware
                           ;; encoding response body
                         muuntaja/format-response-middleware
                           ;; exception handling
                         exception/exception-middleware
                           ;; decoding request body
                         muuntaja/format-request-middleware
                           ;; coercing response bodys
                         coercion/coerce-response-middleware
                           ;; coercing request parameters
                         coercion/coerce-request-middleware
                           ;; multipart
                         multipart/multipart-middleware]}})
   (ring/routes
    (swagger-ui/create-swagger-ui-handler
     {:path "/"
      :config {:validatorUrl nil
               :urls [{:name "swagger", :url "swagger.json"}
                      {:name "openapi", :url "openapi.json"}]
               :urls.primaryName "openapi"
               :operationsSorter "alpha"}})
    (ring/create-default-handler))
   ;; {:executor sieppari/executor}
   ))

(defonce server (atom nil))
(defn start []
  (reset! server
          (jetty/run-jetty #'app {:port 3000, :join? false, :async true})
        ;; (aleph/start-server (aleph/wrap-ring-async-handler #'app) {:port 3000})
        ;; (println "server running in port 3000")
          ))
(defn stop []
  (when-some [s @server]
    (.stop s)
    (reset! server nil)))

(comment
  (start))

(defn -main [& args]
  (println "Kopdar server now running..")
  (start))
