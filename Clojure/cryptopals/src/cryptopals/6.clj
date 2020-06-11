(ns cryptopals.6)

(require '[cryptopals.lib.xor :as x])
(require '[cryptopals.lib.base64 :as b])

(def code (->> (slurp "src/cryptopals/6.txt")
               (clojure.string/split-lines)
               (apply str)
               (b/b64decode)))

(defn get-normalized-weight
  "Return average normalized weight of a string sequence grouped by key size k
   Weight is averaged by (1+2)+(2+3)+...+(k-1)+k / (count of sequence in the numerator)"
  [c k]
  (let [sq (re-seq (re-pattern (str ".{"k"}")) c)
        average (partial (fn [xs] (/ (float (reduce + xs)) (count xs))))]
    (loop [x sq r []]
      (let [[a b & more] x]
        (if (or (empty? a) (empty? b)) (/ (float (average r)) k)
            (recur (rest x) (conj r (x/hammingdistance a b))))))))

(defn guess-key-size
  "Builds and returns a sorted list of keysize sorted by normalizedweight.
   The lowest key of first 4-5 values is probably the key."
  [c]
  (->> (range 2 41)
       (map-indexed (fn[a b] {(+ a 2) (get-normalized-weight c b)}))
       (apply merge)
       (sort-by val)
       (map first)))

(defn transpose-code
  "Transpose the code into vectors of strings where code is split into blocks of size k
   then, create sub strings formed by chars 1 of all blocks ... char k of all blocks"
  [c k]
  (let [sq (apply vector (re-seq (re-pattern (str ".{"k"}")) c))]
    (loop [i 0 r []]
      (if (< i k)
        (recur (inc i) (conj r (apply str (map #(nth % i) sq)))) r))))

