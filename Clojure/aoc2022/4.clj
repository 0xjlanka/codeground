(require '[clojure.set :as set])

(defn to_rangeset
  "Converts string in the format of A-B into a set of range
  of numbers between A-B+1"
  [x] ; In the format "x-y"
  (let [a (clojure.string/split x #"-")]
    (set
     (range
      (Integer/parseInt (str (first a)))
      (+ (Integer/parseInt (str (last a))) 1)))))

(defn is_subset?
  "Checks if the passed vector containing two range sets
  are subsets of each other"
  [x]
  (let [a (to_rangeset (first x))
        b (to_rangeset (last x))]
    ;(prn (sort a) "-" (sort  b))
    ;(prn (set/subset? a b) "-" (set/subset? b a))
    (or (set/subset? a b) (set/subset? b a))))

(defn overlap?
  "Checks if the passed vector containing two range sets
  overlap each other"
  [x]
  (let [a (to_rangeset (first x))
        b (to_rangeset (last x))]
    ;(prn (sort a) "-" (sort  b))
    ;(prn (set/intersection a b))
    (not (empty? (set/intersection a b)))))

(time
 (let [a (clojure.string/split (slurp "4.txt") #"\n")
       b (for [x a
               :let [y (clojure.string/split x #",")]] y)]
   (prn (count (filter true? (map is_subset? b))))
   (prn (count (filter true? (map overlap? b))))))
