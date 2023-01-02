(defn isintstr?
  "Returns true of this string contains all numbers. False otherwise"
  [s]
  (every? #(and (> % 47) (< % 58)) (map int (seq s))))

(defn rupdate
  "Recursively update parent directory sizes with this file entry size"
  [cd dirs l]
  (loop [d cd
         dl {}]
    (if (empty? d) (conj dirs dl)
        (recur
         (clojure.string/join "/" (drop-last (clojure.string/split d #"/")))
         (conj dl {d (+ (if (nil? (dirs d)) 0 (dirs d)) (Integer/parseInt (first l)))})))))

(defn getdirs
  "Returns the hash map of absolute dir path and size"
  [a]
  (loop [lines a
         cd ""
         dirs {}]
    (if (empty? lines) dirs
        (do (let [l (clojure.string/split (clojure.string/trim (first lines)) #" ")]
              (if (and (= (first l) "$") (= (second l) "cd"))
                (case (get l 2)
                  ".." (recur (rest lines) (clojure.string/join "/" (drop-last (clojure.string/split cd #"/"))) dirs)
                  (recur (rest lines) (str cd "/" (get l 2)) dirs))
                (if (isintstr? (first l))
                  (recur (rest lines) cd (rupdate cd dirs l))
                  (recur (rest lines) cd dirs))))))))


(time
 (let [a (clojure.string/split (slurp "7.txt") #"\n")
       dirmap (getdirs a)
       freesp (- 70000000 (dirmap "//"))]
   (prn (reduce + (filter #(< % 100000) (vals dirmap))))
   (prn (first (sort-by second (filter #(> (+ freesp (second %)) 30000000) dirmap))))))

