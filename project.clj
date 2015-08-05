(defproject monger-connect "0.1.0-SNAPSHOT"
  :description "Making it easier to connect with monger"
  :url "http://github.com/Rafflecopter/monger-connect"
  :license {:name "MIT"
            :url "http://github.com/Rafflecopter/monger-connect/blob/master/LICENSE"}

  :deploy-repositories [["releases" :clojars]]
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [com.novemberain/monger "3.0.0"]])
