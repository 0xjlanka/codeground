(ns cryptopals.1)

(require '[cryptopals.lib.base64 :as b64])

(defn hexify-str
  "Create a hex string out of the ascii string provided in s"
  [s]
  (apply str (map #(format "%02x" (int %)) s)))

(defn unhexify-str
  "Create a ascii string from hex string provided in s"
  [s]
  (apply str (map char (map #(Integer/parseInt % 16) (re-seq #".." s)))))

(defn -main
  [& args]
  (->> "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d"
       (unhexify-str)
       (b64/b64encode)
       (println))
