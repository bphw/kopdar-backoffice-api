(ns server.cache
  (:require [taoensso.carmine :as car :refer (wcar)]))

(defonce my-conn-pool (car/connection-pool {}))
(def     my-conn-spec {:uri "redis://192.3.228.147:6379/"})
(def     my-wcar-opts {:pool my-conn-pool, :spec my-conn-spec})
;;(def server1-conn {:pool {} :sepc {:host "192.3.228.147" :port 6379}})
(defmacro wcar* [& body] `(car/wcar my-wcar-opts ~@body))

(defn setter [key value]
  (wcar* (car/set key value)))

(defn setter-with-exp [key value exp]
  (wcar* (car/set key value))
  (wcar* (car/expire key exp)))

(defn getter [key]
  (wcar* (car/get key)))

(defn ping []
  (wcar* (car/ping)))
