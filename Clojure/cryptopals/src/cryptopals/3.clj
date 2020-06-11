(ns cryptopals.3)

(def c "1b37373331363f78151b7f2b783431333d78397828372d363c78373e783a393b3736")

; Convert code into string
(def code (->> c
               (re-seq #"..")
               (map #(Integer/parseInt % 16))
               (map char)
               (apply str)))

(require '[cryptopals.lib.xor :as x])

(defn -main
  [& args]
  (first (x/decode1bytexor code)))
