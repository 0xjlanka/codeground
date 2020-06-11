(ns cryptopals.2)

(def a "1c0111001f010100061a024b53535009181c")
(def b "686974207468652062756c6c277320657965")

(defn -main
  [& args]
  (println
   (apply str
          (map #(format "%02x" %)
               (map #(bit-xor (Integer/parseInt %1 16)
                              (Integer/parseInt %2 16))
                    (re-seq #".." a)
                    (re-seq #".." b))))))
