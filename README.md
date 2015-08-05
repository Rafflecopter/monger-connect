# monger-connect

A Clojure library designed to make using monger easier.

## Usage

```clojure
(require '[monger.connect :refer [db wmong]]
         '[monger.collection :as mc])

(wmong {:dbname "test"}
  (mc/find-one-as-map db "testcollection" {:_id someid}))
```

`wmong` is a macro that takes a config map and connects to mongo using monger's native methods. Then `db` will be bound and can be used as a normal db reference from `monger.core/get-db`.

#### Config Map

For a single server:

```clojure
{:host "localhost"
 :port 27017
 :dbname "test"
 :options {:write-concern 1}}
``

`:options` can either be a map of options into `monger.core/mongo-options-builder` or a `MongoClientOptions` object you built yourself.

Authentication:

```clojure
{:dbname "test"
 :user "testuser"
 :pass "testpass"}
```

Or if you want to authenticate with a different database:

```clojure
{:dbname "test"
 :auth-dbname "admin"
 :user "adminuser"
 :pass "adminpass"}
```

Replica Set:

```clojure
{:hosts ["someserver:27017" "otherserver:27017"]}
```

## License

See MIT License in `LICENSE` file.
