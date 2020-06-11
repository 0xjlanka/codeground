(def B64Code
  {0 \A, 1 \B, 2 \C, 3 \D, 4 \E, 5 \F, 6 \G, 7 \H, 8 \I, 9 \J, 10 \K, 11 \L, 12 \M, 13 \N, 14 \O, 15 \P,
   16 \Q, 17 \R, 18 \S, 19 \T, 20 \U, 21 \V, 22 \W, 23 \X, 24 \Y, 25 \Z, 26 \a, 27 \b, 28 \c, 29 \d, 30 \e, 31 \f,
   32 \g, 33 \h, 34 \i, 35 \j, 36 \k, 37 \l, 38 \m, 39 \n, 40 \o, 41 \p, 42 \q, 43 \r, 44 \s, 45 \t, 46 \u, 47 \v,
   48 \w, 49 \x, 50 \y, 51 \z, 52 \0, 53 \1, 54 \2, 55 \3, 56 \4, 57 \5, 58 \6, 59 \7, 60 \8, 61 \9, 62 \+, 63 \/})

(defn char2nbit
  "Converts the input char c into n bit binary padding 0s at the beginning if required and returns it as string"
  [c n]
  ;; Easier way of writing (format (str "%0"n"d") (Integer/parseInt (Integer/toBinaryString (int c))))
  (->> (int c)
       (Integer/toBinaryString)
       (Integer/parseInt)
       (format (str "%0"n"d")))) ; Resolves to %08d or %06d

(defn padstr
  "Check and pad the string s so that the length of bits in s is a multiple of 6 and return the binary string as a vector of group of 6 bits"
  [s]
  (let [bstr (apply str (map #(char2nbit % 8) s))
        remainder (rem (count bstr) 6)
        pad (if (not= remainder 0) (- 6 remainder) 0)
        zeropadstr (str bstr (apply str (repeat pad "0")))]
    (re-seq #"\d{6}" zeropadstr)))

(defn b64encode
  "Encodes a given string into base64"
  [s]
  (let [code (apply str (map #(get B64Code %) (map #(Integer/parseInt % 2) (padstr s))))]
    (loop [c code]
      (if-not (zero? (rem (count c) 4))
        (recur (str c "="))
        c))))


(defn b64decode
  "Decodes a base64 string into string"
  [s]
  (->> (first (clojure.string/split s #"="))
       (map #(get (clojure.set/map-invert B64Code) %))
       (map #(char2nbit % 6))
       (apply str)
       (re-seq #"\d{8}")
       (map #(Integer/parseInt % 2))
       (map char)
       (apply str)))

(defn unhexify-str
  "Create a ascii string from hex string provided in s"
  [s]
  (apply str (map char (map #(Integer/parseInt % 16) (re-seq #".." s)))))

(println
 (->> "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d"
      (unhexify-str)
      (b64encode)))
(println
 (->> "49276d206b696c6c696e6720796f757220627261696e206c696b65206120706f69736f6e6f7573206d757368726f6f6d"
      (unhexify-str)
      (b64encode)
      (b64decode)))
