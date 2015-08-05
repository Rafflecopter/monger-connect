(ns monger.connect-test
  (:require [clojure.test :refer :all]
            [monger.connect :refer [db wmong]]
            [monger.command :as mcommand]
            [monger.db :as mdb]
            [monger.core :as mg]))

(def cfg {:host "localhost"
          :port 27017
          :dbname "test"
          :options {:write-concern com.mongodb.WriteConcern/SAFE}})
(def cfg2 (assoc cfg :dbname "test2"))

(def auth-cfg (merge cfg {:user "testuser"
                          :pass "testpass"}))

(def auth-admin-cfg (merge cfg {:user "adminuser"
                                :pass "adminpass"
                                :auth-dbname "admin"}))

(def auth-fail-cfg (merge cfg {:user "failuser"
                               :pass "failpass"
                               :auth-dbname "fail"
                               :options (let [b (mg/mongo-options-builder nil)]
                                            (.serverSelectionTimeout b 0)
                                            (.build b))}))

(deftest basic-test
  (testing "wmong connects correctly"
    (is (= "test" (get (wmong cfg (mcommand/db-stats db)) "db"))))
  (testing "wmong connects to a different config correctly"
    (is (= "test2" (get (wmong cfg2 (mcommand/db-stats db)) "db"))))
  (testing "wmong defaults well"
    (is (= "yoyo" (get (wmong {:dbname "yoyo"} (mcommand/db-stats db)) "db")))))

(deftest auth-test
  (testing "create test user"
    (wmong cfg (mdb/add-user db "testuser" (.toCharArray "testpass"))))
  (testing "create admin user"
    (wmong (assoc cfg :dbname "admin")
          (mdb/add-user db "adminuser" (.toCharArray "adminpass"))))
  (testing "login with test user"
    (is (= "test" (get (wmong auth-cfg (mcommand/db-stats db)) "db"))))
  (testing "login with admin user"
    (is (= "test" (get (wmong auth-admin-cfg (mcommand/db-stats db)) "db"))))
  (testing "login with fail user"
    (is (= :exception (try (get (wmong auth-fail-cfg (mcommand/db-stats db)) "db")
                        (catch Exception e :exception))))))
