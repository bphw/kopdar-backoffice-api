(defproject ring-example "0.1.0-SNAPSHOT"
  :description "Kopdar Backoffice with Reitit Http and Swagger"
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [ring/ring-jetty-adapter "1.7.1"]
                 [aleph "0.4.7-alpha5"]
                 [metosin/reitit "0.7.0-alpha7"]
                 [metosin/reitit-swagger-ui "0.7.0-alpha7"]
                 [seancorfield/next.jdbc "1.1.610"]
                 [com.mchange/c3p0 "0.9.5.5"]
                 ;; [org.xerial/sqlite-jdbc "3.7.2"]
                 [mysql/mysql-connector-java "8.0.19"]]
  :repl-options {:init-ns server.core
                 :timeout 120000})
