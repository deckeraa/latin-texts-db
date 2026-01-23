(defproject latin-texts-db "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.12.0"]
                 [ring/ring-core "1.13.0"]       
                 ;; [ring/ring-jetty-adapter "1.13.0"]
                 [http-kit/http-kit "2.8.0"]
                 [ring/ring-defaults "0.5.0"]
                 [compojure "1.7.1"]
                 [com.github.seancorfield/next.jdbc "1.3.981"]
                 [org.xerial/sqlite-jdbc "3.49.1.0"]]
  ;; :dependencies [[org.clojure/clojure "1.10.0"]
  ;;                [compojure "1.6.1"]
  ;;                [ring/ring-defaults "0.3.2"]]
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler latin-texts-db.handler/app}
  :profiles
  {:dev {:dependencies [[javax.servlet/servlet-api "2.5"]
                        [ring/ring-mock "0.3.2"]]}})
