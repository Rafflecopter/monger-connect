(ns monger.connect
  (:require [clojure.string :as string]
            [monger.core :as mg]
            [monger.collection :as mc]
            [monger.credentials :as mcred])
  (:import [com.mongodb MongoClientOptions]))

(def ^:private -dbs (atom nil))

(defn get-db [cfg] (get @-dbs cfg))

(def ^:dynamic db nil)


(defn connect [{:keys [hosts host port dbname auth-dbname user pass options] :as cfg
                :or {host "localhost" port 27017 dbname "test"}}]
  (let [addrs (->> (or hosts [(str host ":" port)])
                   (map #(string/split % #":"))
                   (map #(mg/server-address (nth % 0) (-> % (nth 1) Integer/parseInt))))
        options (cond (map? options) (mg/mongo-options options)
                      (instance? MongoClientOptions options) options
                      :else (mg/mongo-options {}))
        creds (if user (mcred/create user (or auth-dbname dbname) pass))
        conn  (if creds (mg/connect addrs options creds) (mg/connect addrs options))
        dbref (mg/get-db conn dbname)]
    (swap! -dbs assoc cfg dbref)
    dbref))

(defmacro wmong [cfg & forms]
  `(let [cfg# ~cfg]
     (binding [db (if-let [db# (get-db cfg#)] db# (connect cfg#))]
       ~@forms)))