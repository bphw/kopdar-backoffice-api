(ns server.db
  (:require [next.jdbc :as jdbc]
            [next.jdbc.connection :as connection]
            [next.jdbc.result-set :as rs]
            [next.jdbc.sql :as sql]
            [server.cache :as c]
            [clojure.spec.alpha :as s]
            [expound.alpha :as expound]
            [clojure.data.json :as json]))

(def mysql
  ;; remove host when loaded to VPS
  (connection/->pool com.mchange.v2.c3p0.ComboPooledDataSource
                     {:dbtype "mysql" :dbname "skalakid_skw" :host "192.3.228.147" :user "bambang" :password "Matengkon1!" :dataSourceProperties {:socketTimeout 30}
                      :maxPoolSize 30})
  )

(def ds
  (jdbc/get-datasource mysql))
(def db
  (jdbc/with-options ds {:builder-fn rs/as-unqualified-lower-maps}))

;; kategori1/kdstblmastkat1
(defn get-all-category-one []
  (jdbc/execute! db ["SELECT * FROM kdstblmastkat1;"]))
(defn get-category-one [id]
  (sql/get-by-id db :kdstblmastkat1 id :tbid {}))
(defn create-category-one [category]
  (sql/insert! db :kdstblmastkat1 category))
(defn update-category-one [body id]
  (sql/update! db :kdstblmastkat1 body {:tbid id}))
(defn delete-category-one [id]
  (sql/delete! db :kdstblmastkat1 {:tbid id}))

;; kategori1/kdstblmastkat2
(defn get-all-category-two []
  (jdbc/execute! db ["SELECT * FROM kdstblmastkat2;"]))
(defn get-category-two [id]
  (sql/get-by-id db :kdstblmastkat2 id :tbid {}))
(defn create-category-two [category]
  (sql/insert! db :kdstblmastkat2 category))
(defn update-category-two [body id]
  (sql/update! db :kdstblmastkat2 body {:tbid id}))
(defn delete-category-two [id]
  (sql/delete! db :kdstblmastkat2 {:tbid id}))

;; kategori1/kdstblmastkat3
(defn get-all-category-three []
  (jdbc/execute! db ["SELECT * FROM kdstblmastkat3;"]))
(defn get-category-three [id]
  (sql/get-by-id db :kdstblmastkat3 id :tbid {}))
(defn create-category-three [category]
  (sql/insert! db :kdstblmastkat3 category))
(defn update-category-three [body id]
  (sql/update! db :kdstblmastkat3 body {:tbid id}))
(defn delete-category-three [id]
  (sql/delete! db :kdstblmastkat3 {:tbid id}))

;; user address/kdstblalmtuser
(defn get-all-user-addresses []
  (jdbc/execute! db ["SELECT * FROM kdstblalmtuser;"]))
(defn get-user-address [id]
  (sql/get-by-id db :kdstblalmtuser id :tbid {}))
(defn create-user-address [addr]
  (sql/insert! db :kdstblalmtuser addr))
(defn update-user-address [body id]
  (sql/update! db :kdstblalmtuser body {:tbid id}))
(defn delete-user-address [id]
  (sql/delete! db :kdstblalmtuser {:tbid id}))

;; payment method/kdstblmastmethbayr
(defn get-all-payment-methods []
  (jdbc/execute! db ["SELECT * FROM kdstblmastmethbayr;"]))
(defn get-payment-method [id]
  (sql/get-by-id db :kdstblmastmethbayr id :tbid {}))
(defn create-payment-method [method]
  (sql/insert! db :kdstblmastmethbayr method))
(defn update-payment-method [body id]
  (sql/update! db :kdstblmastmethbayr body {:tbid id}))
(defn delete-payment-method [id]
  (sql/delete! db :kdstblmastmethbayr {:tbid id}))

;; delivery/kdstblmastpengiriman
(defn get-all-deliveries []
  (jdbc/execute! db ["SELECT * FROM kdstblmastpengiriman;"]))
(defn get-delivery [id]
  (sql/get-by-id db :kdstblmastpengiriman id :tbid {}))
(defn create-delivery [delivery]
  (sql/insert! db :kdstblmastpengiriman delivery))
(defn update-delivery [body id]
  (sql/update! db :kdstblmastpengiriman body {:tbid id}))
(defn delete-delivery [id]
  (sql/delete! db :kdstblmastpengiriman {:tbid id}))

;; status transaksi/kdstblmasttranstat
(defn get-all-trx-statuses []
  (jdbc/execute! db ["SELECT * FROM kdstblmasttranstat;"]))
(defn get-trx-status [id]
  (sql/get-by-id db :kdstblmasttranstat id :tbid {}))
(defn create-trx-status [status]
  (sql/insert! db :kdstblmasttranstat status))
(defn update-trx-status [body id]
  (sql/update! db :kdstblmasttranstat body {:tbid id}))
(defn delete-trx-status [id]
  (sql/delete! db :kdstblmasttranstat {:tbid id}))

;; group user/kdstblmastugrp
(defn get-all-user-groups []
  (jdbc/execute! db ["SELECT * FROM kdstblmastugrp;"]))
(defn get-user-group [id]
  (sql/get-by-id db :kdstblmastugrp id :tbid {}))
(defn create-user-group [group]
  (sql/insert! db :kdstblmastugrp group))
(defn update-user-group [body id]
  (sql/update! db :kdstblmastugrp body {:tbid id}))
(defn delete-user-group [id]
  (sql/delete! db :kdstblmastugrp {:tbid id}))

;; user/kdstblmastuser
(defn get-all-users []
  (jdbc/execute! db ["SELECT * FROM kdstblmastuser;"]))
(defn get-user [id]
  (sql/get-by-id db :kdstblmastuser id :tbid {}))
(defn create-user [user]
  (sql/insert! db :kdstblmastuser user))
(defn update-user [body id]
  (sql/update! db :kdstblmastuser body {:tbid id}))
(defn delete-user [id]
  (sql/delete! db :kdstblmastuser {:tbid id}))

;; stores/kdstblmasttoko
(defn get-all-stores []
  (jdbc/execute! db ["SELECT * FROM kdstblmasttoko;"]))
(defn get-store [id]
  (sql/get-by-id db :kdstblmasttoko id :tbid {}))
(defn create-store [store]
  (sql/insert! db :kdstblmasttoko store))
(defn update-store [body id]
  (sql/update! db :kdstblmasttoko body {:tbid id}))
  ;; (get-store id))
(defn delete-store [id]
  (sql/delete! db :kdstblmasttoko {:tbid id}))

;; etalase/kdstblmastetls
(defn get-all-etalases []
  (jdbc/execute! db ["SELECT * FROM kdstblmastetls"]))
(defn get-etalase [id]
  (sql/get-by-id db :kdstblmastetls id :tbid {}))
(defn create-etalase [etalase]
  (sql/insert! db :kdstblmastetls etalase))
(defn update-etalase [body id]
  (sql/update! db :kdstblmastetls body {:tbid id}))
(defn delete-etalase [id]
  (sql/delete! db :kdstblmastetls {:tbid id}))

;; stores/kdstblmastprdk
(defn get-all-products []
  (jdbc/execute! db ["SELECT * FROM kdstblmastprdk"]))
(defn get-all-products-redis []
  (when (nil? (c/getter "products"))
    (c/setter-with-exp "products" (json/write-str (jdbc/execute! db ["SELECT * FROM kdstblmastprdk"])) 100))
  (s/conform :master/products (json/read-str (c/getter "products"))))
(defn get-product [id]
  (sql/get-by-id db :kdstblmastprdk id :tbid {}))
(defn create-product [product]
  (sql/insert! db :kdstblmastprdk product))
(defn update-product [body id]
  (sql/update! db :kdstblmastprdk body {:tbid id}))
(defn delete-product [id]
  (sql/delete! db :kdstblmastprdk {:tbid id}))
(defn get-product-images [id]
  (sql/find-by-keys db :kdstblmastprdkimag_ {:prdktbid id} {:columns [:imag]}))
(defn get-product-images-arr [id]
  ;; get second var from first object returned
  (map #(second (first %)) (get-product-images id)))
(defn get-product-rating [id]
  (sql/get-by-id db :kdstblmastprdkdeta_ id :prdktbid {}))

;; stores/kdstblrprdkdeta
(defn get-all-product-details []
  (jdbc/execute! db ["SELECT * FROM kdstblrprdkdeta"]))
(defn get-product-detail [id]
  (sql/get-by-id db :kdstblrprdkdeta id :tbid {}))
(defn create-product-detail [detail]
  (sql/insert! db :kdstblrprdkdeta detail))
(defn update-product-detail [body id]
  (sql/update! db :kdstblrprdkdeta body {:tbid id}))
(defn delete-product-detail [id]
  (sql/delete! db :kdstblrprdkdeta {:tbid id}))

;; stores/kdstblrtokhead
(defn get-all-store-headers []
  (jdbc/execute! db ["SELECT * FROM kdstblrtokhead"]))
(defn get-store-header [id]
  (sql/get-by-id db :kdstblrtokhead id :tbid {}))
(defn create-store-header [header]
  (sql/insert! db :kdstblrtokhead header))
(defn update-store-header [body id]
  (sql/update! db :kdstblrtokhead body {:tbid id}))
(defn delete-store-header [id]
  (sql/delete! db :kdstblrtokhead {:tbid id}))

;; stores/kdstbltchathead
(defn get-all-chat-headers []
  (jdbc/execute! db ["SELECT * FROM kdstbltchathead"]))
(defn get-chat-header [id]
  (sql/get-by-id db :kdstbltchathead id :tbid {}))
(defn create-chat-header [header]
  (sql/insert! db :kdstbltchathead header))
(defn update-chat-header [body id]
  (sql/update! db :kdstbltchathead body {:tbid id}))
(defn delete-chat-header [id]
  (sql/delete! db :kdstbltchathead {:tbid id}))

;; stores/kdstbltjuadeta
(defn get-all-trx-details []
  (jdbc/execute! db ["SELECT * FROM kdstbltjuadeta"]))
(defn get-trx-detail [id]
  (sql/get-by-id db :kdstbltjuadeta id :tbid {}))
(defn create-trx-detail [detail]
  (sql/insert! db :kdstbltjuadeta detail))
(defn update-trx-detail [body id]
  (sql/update! db :kdstbltjuadeta body {:tbid id}))
(defn delete-trx-detail [id]
  (sql/delete! db :kdstbltjuadeta {:tbid id}))

;; stores/kdstbltjuahead
(defn get-all-trx-headers []
  (jdbc/execute! db ["SELECT * FROM kdstbltjuahead"]))
(defn get-trx-header [id]
  (sql/get-by-id db :kdstbltjuahead id :tbid {}))
(defn create-trx-header [header]
  (sql/insert! db :kdstbltjuahead header))
(defn update-trx-header [body id]
  (sql/update! db :kdstbltjuahead body {:tbid id}))
(defn delete-trx-header [id]
  (sql/delete! db :kdstbltjuahead {:tbid id}))
