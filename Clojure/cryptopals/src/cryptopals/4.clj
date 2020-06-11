(ns cryptopals.4)

(require '[cryptopals.lib.xor :as x])

(def code (clojure.string/split-lines (slurp "src/cryptopals/4.txt")))

(defn -main
  [& args]
  (->> (for [c code
             :let [cstr (apply str (map char (map #(Integer/parseInt % 16) (re-seq #".." c))))
                   best (first (x/decode1bytexor cstr))]]
         best)
       (sort-by second)
       (reverse)
       (first)
       (println)))
