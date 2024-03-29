(defproject kopdar-swagger "0.1.0-SNAPSHOT"
  :description "Kopdar Backoffice with Reitit Http and Swagger"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [ring/ring-jetty-adapter "1.7.1"]
                 [aleph "0.4.7-alpha5"]
                 [metosin/reitit "0.7.0-alpha7"]
                 [metosin/reitit-swagger-ui "0.7.0-alpha7"]
                 [seancorfield/next.jdbc "1.1.610"]
                 [com.mchange/c3p0 "0.9.5.5"]
                 [mysql/mysql-connector-java "8.0.19"]
                 [com.taoensso/carmine "3.3.2"]
                 [expound "0.9.0"]
                 [org.clojure/data.json "2.5.0"]]
  :repl-options {:init-ns server.core
                 :timeout 120000}
  :main server.core
  :aot [server.core])
