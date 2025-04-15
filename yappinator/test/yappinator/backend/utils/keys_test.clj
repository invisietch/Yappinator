(ns yappinator.backend.utils.keys-test
  (:require [clojure.test :refer [deftest is testing]]
            [yappinator.backend.utils.keys :refer [namespace-keys-recursive denamespace-keys-recursive]]))

(deftest test-namespace-keys-recursive
  (testing "Basic namespacing"
    (is (= (namespace-keys-recursive {:a 1 :b 2} "ns")
           {:ns/a 1 :ns/b 2})))

  (testing "Nested namespacing"
    (is (= (namespace-keys-recursive {:a {:b {:c 3}}} "ns")
           {:ns/a {:ns/b {:ns/c 3}}})))

  (testing "Namespacing with exclusions"
    (is (= (namespace-keys-recursive {:id 1 :a 2} "ns" #{:id})
           {:id 1 :ns/a 2})))

  (testing "Nested with exclusions"
    (is (= (namespace-keys-recursive {:id 1 :nested {:skip 2 :keep 3}} "ns" #{:id :skip})
           {:id 1 :ns/nested {:skip 2 :ns/keep 3}})))

  (testing "Vectors of maps"
    (is (= (namespace-keys-recursive {:items [{:a 1} {:b 2}]} "ns")
           {:ns/items [{:ns/a 1} {:ns/b 2}]})))

  (testing "Vectors with nested maps and exclusions"
    (is (= (namespace-keys-recursive {:items [{:id 1 :val 2} {:id 3 :val 4}]} "ns" #{:id})
           {:ns/items [{:id 1 :ns/val 2} {:id 3 :ns/val 4}]})))

  (testing "Strings not turned into seqs"
    (is (= (namespace-keys-recursive {:description "character"} "ns")
           {:ns/description "character"}))))

(deftest test-denamespace-keys-recursive
  (testing "Simple map"
    (is (= (denamespace-keys-recursive {:character/name "Alice"})
           {:name "Alice"})))

  (testing "Nested map"
    (is (= (denamespace-keys-recursive {:character/avatar {:avatar/url "http://example.com/avatar.png"}})
           {:avatar {:url "http://example.com/avatar.png"}})))

  (testing "Deeply nested map"
    (is (= (denamespace-keys-recursive {:a/b {:c/d {:e/f 42}}})
           {:b {:d {:f 42}}})))

  (testing "Vector of maps"
    (is (= (denamespace-keys-recursive {:characters [{:character/name "Alice"}
                                                     {:character/name "Bob"}]})
           {:characters [{:name "Alice"}
                         {:name "Bob"}]})))

  (testing "Mixed vectors and nested maps"
    (is (= (denamespace-keys-recursive {:character/list [{:character/item {:item/id 1}}
                                                         {:character/item {:item/id 2}}]})
           {:list [{:item {:id 1}}
                   {:item {:id 2}}]})))

  (testing "Handles non-map/vector types unchanged"
    (is (= (denamespace-keys-recursive {:character/name "Alice"
                                        :character/tags ["tag1" "tag2"]
                                        :character/age 30
                                        :character/active? true})
           {:name "Alice"
            :tags ["tag1" "tag2"]
            :age 30
            :active? true}))))

