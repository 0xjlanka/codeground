(ns cryptopals.lib.xor)

(defn xor-strs
  "Xors strings a and b of equal length by converting them into bytes and returns the result as a list"
  [a b]
  (map #(bit-xor (int %1) (int %2)) a b))

(defn englishness
  "This function returns a value between 0 and 1 based on how englishlike the str is
  The algorithm is based on Bhattacharya Coefficient described here:
  https://github.com/Lukasa/cryptopals/blob/master/cryptopals/challenge_one/three.py#L78"
  [s]
  (let [ustr (clojure.string/upper-case s)
        ctable (frequencies (apply str (re-seq #"[\p{Alpha}\x20]" ustr)))
        freqtable1 {\A 0.0651738, \B 0.0124248, \C 0.0217339, \D 0.0349835,
                    \E 0.1041442, \F 0.0197881, \G 0.0158610, \H 0.0492888,
                    \I 0.0558094, \J 0.0009033, \K 0.0050529, \L 0.0331490,
                    \M 0.0202124, \N 0.0564513, \O 0.0596302, \P 0.0137645,
                    \Q 0.0008606, \R 0.0497563, \S 0.0515760, \T 0.0729357,
                    \U 0.0225134, \V 0.0082903, \W 0.0171272, \X 0.0013692,
                    \Y 0.0145984, \Z 0.0007836, \space 0.1918182}
        freqtable {\A 0.08167, \B 0.01492, \C 0.02782, \D 0.04253, \E 0.1270, \F 0.02228,
                   \G 0.02015, \H 0.06094, \I 0.06966, \J 0.00153, \K 0.00772, \L 0.04025,
                   \M 0.02406, \N 0.06749, \O 0.07507, \P 0.01929, \Q 0.00095, \R 0.05987,
                   \S 0.06327, \T 0.09056, \U 0.02758, \V 0.00978, \W 0.02360, \X 0.00150,
                   \Y 0.01974, \Z 0.00074, \space 0.1918182}]

    (reduce + (for [[k v] ctable
                    :let [j (Math/sqrt (/ (* (get freqtable1 k) v) (count ustr)))]]
                j))))

(defn decode1bytexor
  "codestr - the hexadecimal code string which is 1 byte xor encoded"
  [codestr]
  (reverse (sort-by second
                    (for [idx (range 0 256)
                          :let [gen1bytestr (partial (fn[n count] (apply str (repeat count (char n)))))
                                string (apply str (map char (xor-strs codestr (gen1bytestr idx (count codestr)))))
                                ;non_print (count (re-seq #"[^\p{Print}^\p{Space}]" string))
                                escore (englishness string)]
                          :when (> escore 0.7)]
                      [(char idx) escore string]))))

(defn hammingdistance
  "Returns the hamming distance between two strings"
  [a b]
  (->> b
       (xor-strs a)
       (map int)
       (map #(Integer/toBinaryString %))
       (apply str)
       (filter #(= % \1))
       (count)))
