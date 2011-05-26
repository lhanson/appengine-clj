(ns appengine.test.urlfetch
  (:require [appengine.urlfetch :as uf])
  (:use appengine.test clojure.test)
  (:import [com.google.appengine.api.urlfetch
            URLFetchServiceFactory
            URLFetchService]))

(urlfetch-test test-urlfetch-service
  (is (not (nil? (uf/urlfetch)))))

(urlfetch-test test-fetch
  (let [res (uf/fetch "http://www.google.com/")]
    (is (= (:status-code res) 200))
    (is (= (class (:headers res)) clojure.lang.PersistentArrayMap)))
  (let [res (uf/fetch "http://google.com/" {:follow-redirects false})]
    (is (= (:status-code res) 301)))
  (let [res (uf/fetch "http://www.google.com/" {:method "POST"})]
    (is (= (:status-code res) 405))
    (is (= (-> res :headers :Allow) "GET, HEAD"))))
