(require 'clojure.set)

(defn getprio
  [x]
  (cond
    (< (int x) 91) (- (int x) 38) ; prio for upper case letters
    (< (int x) 123) (- (int x) 96))) ; prio for lower case letters

(time
 (let [a (clojure.string/split (slurp "3.txt") #"\n")
       b (for [x a ; for each string
               :let [y (map set (split-at (/ (count x) 2) x))]]
           (clojure.set/intersection (first y) (last y)))
       c (for [x b
               :let [y (first x)]] y)
       d (map getprio c)]
   (prn (apply + d))))

(time
 (let [a (clojure.string/split (slurp "3.txt") #"\n")
       b (partition 3 (map set a))
       c (for [x b
               :let [y (apply clojure.set/intersection x)]] (first y))
       d (map getprio c)]
   (prn (apply + d))))


