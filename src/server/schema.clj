(ns server.schema
  (:require [clojure.spec.alpha :as s]))

;; person schema
(s/def :person/emai string?)
(s/def :person/telp string?)
;; foreign key
(s/def :foreign/almttbid (s/nilable int?))
(s/def :foreign/etlstbid (s/nilable int?))
(s/def :foreign/tokotbid (s/nilable int?))
(s/def :foreign/kat1tbid (s/nilable int?))
(s/def :foreign/kat2tbid (s/nilable int?))
(s/def :foreign/kat3tbid (s/nilable int?))
(s/def :foreign/usertbid (s/nilable int?))
(s/def :foreign/ugrptbid (s/nilable int?))
(s/def :foreign/prdktbid (s/nilable int?))
(s/def :foreign/custtbid (s/nilable int?))
(s/def :foreign/sendtbid (s/nilable int?))
(s/def :foreign/rcpttbid (s/nilable int?))
(s/def :foreign/kirimtbid (s/nilable int?))
(s/def :foreign/mbyrtbid (s/nilable int?))
;; generic schema
(s/def :generic/tbid int?)
(s/def :generic/nama string?)
(s/def :generic/stat (s/nilable string?))
(s/def :generic/trno (s/nilable string?))
(s/def :generic/trit (s/nilable string?))
(s/def :generic/kode (s/nilable string?))
(s/def :generic/imag (s/nilable string?))
(s/def :generic/ratg (s/nilable double?))
(s/def :generic/stde (s/nilable string?))
(s/def :generic/dsc1 (s/nilable string?))
(s/def :generic/dsc2 (s/nilable string?))
(s/def :generic/dsc3 (s/nilable string?))
(s/def :generic/crby (s/nilable string?))
(s/def :generic/crdt (s/nilable string?))
(s/def :generic/crtm (s/nilable string?))
(s/def :generic/chby (s/nilable string?))
(s/def :generic/chdt (s/nilable string?))
(s/def :generic/chtm (s/nilable string?))
;; user schema
(s/def :user/pswd (s/nilable string?))
;; address schema
(s/def :address/jln1 (s/nilable string?))
(s/def :address/jln2 (s/nilable string?))
(s/def :address/kelu (s/nilable string?))
(s/def :address/kota (s/nilable string?))
(s/def :address/prop (s/nilable string?))
(s/def :address/ctry (s/nilable string?))
(s/def :address/kpos (s/nilable string?))
;; delivery schema
(s/def :delivery/grp1 (s/nilable string?))
(s/def :delivery/buom (s/nilable string?))
(s/def :delivery/puom (s/nilable double?))
(s/def :delivery/huom (s/nilable double?))
(s/def :delivery/bjrk (s/nilable string?))
(s/def :delivery/pjrk (s/nilable double?))
(s/def :delivery/hjrk (s/nilable double?))
(s/def :delivery/hkir (s/nilable double?))
;; product schema
(s/def :product/hbli (s/nilable double?))
(s/def :product/hjua (s/nilable double?))
(s/def :product/qstk (s/nilable double?))
(s/def :product/imag string?)
;; product-detail schema
(s/def :product-detail/prdkkode (s/nilable string?))
(s/def :product-detail/prdknama (s/nilable string?))
(s/def :product-detail/prdkhjua (s/nilable double?))
(s/def :product-detail/item (s/nilable double?))
(s/def :product-detail/quan (s/nilable double?))
(s/def :product-detail/disc (s/nilable double?))
(s/def :product-detail/potg (s/nilable double?))
(s/def :product-detail/pajk (s/nilable double?))
(s/def :product-detail/gamt (s/nilable double?))
(s/def :product-detail/namt (s/nilable double?))
;; chat schema
(s/def :chat/subj (s/nilable string?))
;; transaction header schema
(s/def :trx-header/quanitem (s/nilable double?))
(s/def :trx-header/quantota (s/nilable double?))
(s/def :trx-header/disctota (s/nilable double?))
(s/def :trx-header/potgtota (s/nilable double?))
(s/def :trx-header/pajktota (s/nilable double?))
(s/def :trx-header/gamttota (s/nilable double?))
(s/def :trx-header/namttota (s/nilable double?))
(s/def :trx-header/hkir (s/nilable double?))
;; product-rating schema
(s/def :product-detail/favorite (s/nilable boolean?))
(s/def :product-detail/popular (s/nilable boolean?))

;; kdstblmastetls schema
(s/def :master/etalase
  (s/keys
   :req-un [:generic/tbid]
   :opt-un [:generic/kode :generic/nama
            :generic/dsc1 :generic/dsc2 :generic/dsc3 :generic/stde
            :generic/crby :generic/crdt :generic/crtm
            :generic/chby :generic/chdt :generic/chtm]))

(s/def :master/etalases
  (s/coll-of :master/etalase :into []))

;; kdstblmastkat1 schema
(s/def :master/category-one
  (s/keys
   :req-un [:generic/tbid]
   :opt-un [:generic/kode :generic/nama
            :generic/dsc1 :generic/dsc2 :generic/dsc3 :generic/stde
            :generic/crby :generic/crdt :generic/crtm
            :generic/chby :generic/chdt :generic/chtm]))

(s/def :master/category-ones
  (s/coll-of :master/category-one :into []))

;; kdstblmastkat2 schema
(s/def :master/category-two
  (s/keys
   :req-un [:generic/tbid]
   :opt-un [:generic/kode :generic/nama
            :generic/dsc1 :generic/dsc2 :generic/dsc3 :generic/stde
            :generic/crby :generic/crdt :generic/crtm
            :generic/chby :generic/chdt :generic/chtm]))

(s/def :master/category-twos
  (s/coll-of :master/category-two :into []))

;; kdstblmastkat3 schema
(s/def :master/category-three
  (s/keys
   :req-un [:generic/tbid]
   :opt-un [:generic/kode :generic/nama
            :generic/dsc1 :generic/dsc2 :generic/dsc3 :generic/stde
            :generic/crby :generic/crdt :generic/crtm
            :generic/chby :generic/chdt :generic/chtm]))

(s/def :master/category-threes
  (s/coll-of :master/category-three :into []))

;; kdstblalmtuser schema
(s/def :master/user-address
  (s/keys
   :req-un [:generic/tbid]
   :opt-un [:foreign/tokotbid :foreign/usertbid
            :generic/kode :generic/nama
            :foreign/kat1tbid :foreign/kat2tbid :foreign/kat3tbid
            :address/jln1 :address/jln2 :address/kelu
            :address/kota :address/prop :address/ctry :address/kpos
            :generic/dsc1 :generic/dsc2 :generic/dsc3 :generic/stde
            :generic/crby :generic/crdt :generic/crtm
            :generic/chby :generic/chdt :generic/chtm]))

(s/def :master/user-addresses
  (s/coll-of :master/user-address :into []))

;; kdstblmastmethbayr schema
(s/def :master/payment-method
  (s/keys
   :req-un [:generic/tbid]
   :opt-un [:generic/kode :generic/nama
            :generic/dsc1 :generic/dsc2 :generic/dsc3
            :generic/imag :generic/stde
            :generic/crby :generic/crdt :generic/crtm
            :generic/chby :generic/chdt :generic/chtm]))

(s/def :master/payment-methods
  (s/coll-of :master/payment-method))

;; kdstblmastpengiriman schema
(s/def :master/delivery
  (s/keys
   :req-un [:generic/tbid]
   :opt-un [:generic/kode :generic/nama
            :delivery/grp1 :delivery/buom :delivery/puom :delivery/huom
            :delivery/bjrk :delivery/pjrk :delivery/hjrk :delivery/hkir
            :generic/dsc1 :generic/dsc2 :generic/dsc3 :generic/stde
            :generic/crby :generic/crdt :generic/crtm
            :generic/chby :generic/chdt :generic/chtm]))

(s/def :master/deliveries
  (s/coll-of :master/delivery))

;; kdstblmasttranstat schema
(s/def :master/trx-status
  (s/keys
   :req-un [:generic/tbid]
   :opt-un [:generic/kode :generic/nama
            :generic/dsc1 :generic/dsc2 :generic/dsc3
            :generic/imag :generic/stde
            :generic/crby :generic/crdt :generic/crtm
            :generic/chby :generic/chdt :generic/chtm]))

(s/def :master/trx-statuses
  (s/coll-of :master/trx-status))

;; kdstblmastugrp schema
(s/def :master/user-group
  (s/keys
   :req-un [:generic/tbid]
   :opt-un [:generic/kode :generic/nama
            :generic/dsc1 :generic/dsc2 :generic/dsc3
            :generic/imag :generic/stde
            :generic/crby :generic/crdt :generic/crtm
            :generic/chby :generic/chdt :generic/chtm]))

(s/def :master/user-groups
  (s/coll-of :master/user-group))

;; kdstblmastuser schema
(s/def :master/user
  (s/keys
   :req-un [:generic/tbid]
   :opt-un [:foreign/tokotbid :foreign/ugrptbid
            :generic/kode :generic/nama :foreign/almttbid
            :person/emai :person/telp :user/pswd
            :generic/dsc1 :generic/dsc2 :generic/dsc3
            :generic/imag :generic/stde
            :generic/crby :generic/crdt :generic/crtm
            :generic/chby :generic/chdt :generic/chtm]))

(s/def :master/users
  (s/coll-of :master/user))

;; kdstblrprdkdeta schema
(s/def :rate/product-detail
  (s/keys
   :req-un [:generic/tbid]
   :opt-un [:generic/trno :generic/trit :generic/ratg
            :generic/dsc1 :generic/dsc2 :generic/dsc3 :generic/stde
            :generic/crby :generic/crdt :generic/crtm
            :generic/chby :generic/chdt :generic/chtm]))
(s/def :rate/product-details
  (s/coll-of :rate/product-detail))

;; kdstblrtokhead schema
(s/def :rate/store-header
  (s/keys
   :req-un [:generic/tbid]
   :opt-un [:generic/trno :generic/ratg
            :generic/dsc1 :generic/dsc2 :generic/dsc3 :generic/stde
            :generic/crby :generic/crdt :generic/crtm
            :generic/chby :generic/chdt :generic/chtm]))
(s/def :rate/store-headers
  (s/coll-of :rate/store-header))

;; kdstbltchathead schema
(s/def :trx/chat-header
  (s/keys
   :req-un [:generic/tbid]
   :opt-un [:generic/trno :generic/trit
            :foreign/tokotbid :foreign/usertbid :foreign/custtbid :foreign/sendtbid :foreign/rcpttbid
            :chat/subj :generic/stat
            :generic/dsc1 :generic/dsc2 :generic/dsc3 :generic/stde
            :generic/crby :generic/crdt :generic/crtm
            :generic/chby :generic/chdt :generic/chtm]))
(s/def :trx/chat-headers
  (s/coll-of :trx/chat-header))

;; kdstbltjuadeta schema
(s/def :trx/selling-detail
  (s/keys
   :req-un [:generic/tbid]
   :opt-un [:generic/trno :generic/trit
            :foreign/prdktbid
            :product-detail/prdkkode :product-detail/prdknama
            :product-detail/prdkhjua :product-detail/item :product-detail/quan
            :product-detail/disc :product-detail/potg :product-detail/pajk
            :product-detail/gamt :product-detail/namt
            :generic/dsc1 :generic/dsc2 :generic/dsc3 :generic/stde
            :generic/crby :generic/crdt :generic/crtm
            :generic/chby :generic/chdt :generic/chtm]))
(s/def :trx/selling-details
  (s/coll-of :trx/selling-detail))

;; kdstbltjuahead schema
(s/def :trx/selling-header
  (s/keys
   :req-un [:generic/tbid]
   :opt-un [:generic/trno
            :foreign/tokotbid
            :foreign/custtbid
            :foreign/almttbid
            :foreign/kirimtbid
            :foreign/mbyrtbid
            :trx-header/quanitem :trx-header/quantota
            :trx-header/disctota :trx-header/potgtota
            :trx-header/pajktota :trx-header/gamttota :trx-header/namttota :trx-header/hkir
            :generic/dsc1 :generic/dsc2 :generic/dsc3 :generic/stde :generic/stat
            :generic/crby :generic/crdt :generic/crtm
            :generic/chby :generic/chdt :generic/chtm]))
(s/def :trx/selling-headers
  (s/coll-of :trx/selling-header))

;; kdstblmasttoko schema
(s/def :master/store
  (s/keys
   :req-un [:generic/tbid :generic/nama :person/emai :person/telp]
   :opt-un [:foreign/usertbid :generic/kode :foreign/almttbid :generic/imag
            :generic/dsc1 :generic/dsc2 :generic/dsc3 :generic/stde
            :generic/ratg :generic/crby :generic/crdt :generic/crtm
            :generic/chby :generic/chdt :generic/chtm]))

(s/def :master/stores
  (s/coll-of :master/store :into []))

;; kdstblmastprdk schema
(s/def :master/product
  (s/keys
   :req-un [:generic/tbid :generic/kode :generic/nama]
   :opt-un [:foreign/tokotbid :foreign/etlstbid
            :foreign/kat1tbid :foreign/kat2tbid :foreign/kat3tbid
            :product/hjua :product/hbli :product/qstk :generic/ratg
            :generic/dsc1 :generic/dsc2 :generic/dsc3
            :generic/imag :generic/stde
            :generic/crby :generic/crdt :generic/crtm
            :generic/chby :generic/chdt :generic/chtm]))
(s/def :master/products
  (s/coll-of :master/product))

;; product detail contains images, favorite, popular and rating
(s/def :product/images
  (s/coll-of :product/imag :into []))
(s/def :master/product-images-rating
  (s/keys
   :req-un [:foreign/tokotbid]
   :opt-un [:generic/images
            :product-detail/favorite
            :product-detail/popular
            :generic/ratg]))
