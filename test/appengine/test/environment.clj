(ns appengine.test.environment
  (:import (com.google.apphosting.api ApiProxy ApiProxy$Environment)
           (com.google.appengine.tools.development ApiProxyLocalFactory LocalServerEnvironment))
  (:use clojure.test appengine.environment))

(def *appengine-web-xml-path* "test/fixtures/appengine-web.xml")

(deftest test-login-aware-proxy
  (let [proxy (login-aware-proxy {})]
    (is (isa? (class proxy) ApiProxy$Environment))
    (is (= (.getAppId proxy) "local"))))

(deftest test-local-server-environment
  (let [environment (local-server-environment)]
    (is (isa? (class environment) LocalServerEnvironment))
    (is (= (.getAppDir environment) (java.io.File. (System/getProperty "java.io.tmpdir"))))
    (with-appengine (local-proxy)
      (is (= (.enforceApiDeadlines environment) true))))
  (let [environment (local-server-environment ".")]
    (is (isa? (class environment) LocalServerEnvironment))
    (is (= (.getAppDir environment) (java.io.File. ".")))))

(deftest test-init-appengine
  (let [proxy (init-appengine)]))

(deftest test-parse-configuration
  (let [configuration (parse-configuration *appengine-web-xml-path*)]
    (is (= (:application configuration) "appengine"))
    (is (= (:version configuration) "1"))
    (is (= (:properties configuration)
           {"myapp.notify-url" "http://www.example.com/signupnotify"
            "myapp.notify-every-n-signups" "1000"
            "myapp.maximum-message-length" "140"}))))

(deftest test-set-system-properties
  (set-system-properties {"property-name" "property-value"})
  (is (= (System/getProperty "property-name") "property-value")))

(deftest test-with-configuration
  (with-configuration *appengine-web-xml-path*
    (is (= *application* "appengine"))
    (is (= *version* "1"))
    (is (= (System/getProperty "myapp.notify-url") "http://www.example.com/signupnotify"))
    (is (= (System/getProperty "myapp.notify-every-n-signups") "1000"))
    (is (= (System/getProperty "myapp.maximum-message-length") "140"))))



