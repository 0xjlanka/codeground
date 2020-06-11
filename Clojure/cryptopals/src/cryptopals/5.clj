(ns cryptopals.5)

(require '[cryptopals.lib.xor :as x])

(def text "Burning 'em, if you ain't quick and nimble
I go crazy when I hear a cymbal")

(defn generate-repeated-string
  "Generate a string by repeating string s n number of times"
  [s n]
  (apply str (take n (cycle s))))

(defn -main
  [& args]
  (->> (generate-repeated-string "ICE" (count text))
       (x/xor-strs text)
       (map #(format "%02x" %))
       (apply str)))

