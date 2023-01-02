(defn stackcrates
  [model moves stacks]
  (loop [m moves
         s stacks]
    ;(prn s)
    (if (empty? m) s
        (do
          (let [[num from to] (first m)
                numi (Integer/parseInt num)
                fromi (- (Integer/parseInt from) 1)
                toi (- (Integer/parseInt to) 1)
                fromv (get s fromi)
                tov (get s toi)]
                                        ;(prn numi" from "fromv "-" tov)
            (if (= model 9000)
              (do (let [res (loop [x numi
                                   f fromv
                                   v tov]
                              (if (zero? x) [f v]
                                  (recur (dec x) (pop f) (conj v (peek f)))))]
                    (recur (rest m) (assoc s fromi (first res) toi (last res)))))
              (recur (rest m) (assoc s
                                     fromi
                                     (apply vector (drop-last numi fromv))
                                     toi
                                     (apply vector (concat tov (take-last numi fromv)))))))))))


(time
 (let [a (clojure.string/split (slurp "5-moves.txt") #"\n")
       b (clojure.string/split (slurp "5-stacks.txt") #"\n")
       moves (map #(re-seq #"\d+" %) a)
       stacks (apply vector (map #(apply vector %) b))]
   (prn (apply str (map last (stackcrates 9000 moves stacks))))
   (prn (apply str (map last (stackcrates 9001 moves stacks))))))
