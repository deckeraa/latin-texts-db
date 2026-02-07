(defproject latin-texts "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.12.0"]
                 [ring/ring-core "1.13.0"]       
                 ;; [ring/ring-jetty-adapter "1.13.0"]
                 [http-kit/http-kit "2.8.0"]
                 [ring/ring-defaults "0.5.0"]
                 [ring/ring-json "0.5.1"]
                 [compojure "1.7.1"]
                 [com.github.seancorfield/next.jdbc "1.3.981"]
                 [org.xerial/sqlite-jdbc "3.49.1.0"]
                 [migratus "1.0.1"]
                 [com.github.seancorfield/honeysql "2.7.1364"]
                 [thheller/shadow-cljs "3.3.5"]
                 [reagent "2.0.1"]
                 [cljs-bean "1.9.0"]
                 [funcool/promesa "12.0.0-RC2"]
                 [com.cognitect/transit-clj "1.0.333"]
                 [ring-middleware-format "0.7.5"]
                 ]
  :source-paths ["src/clj" "src/cljs" "src/cljc"]
  :resource-paths ["resources"]
  :plugins [[lein-ring "0.12.5"]
            [migratus-lein "0.7.3"]]
  :ring {:handler latin-texts.handler/app}
  :migratus {:store :database
             :migration-dir "migrations"
             :db {:dbtype "sqlite"
                  :dbname "resources/db/latin.db"}}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]
                        [binaryage/devtools "1.0.7"]]}})
