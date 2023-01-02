
(defn row-score [row]
  (let [rps-score (case (clojure.string/upper-case row)
                    "A X" 3
                    "A Y" 6
                    "A Z" 0
                    "B X" 0
                    "B Y" 3
                    "B Z" 6
                    "C X" 6
                    "C Y" 0
                    "C Z" 3
                    :error)
        hand-score (case (clojure.string/upper-case (last row))
                     "X" 1
                     "Y" 2
                     "Z" 3
                     :error)]
    (+ rps-score hand-score)))

(defn b-row-score [row]
  (let [rps-score (case (clojure.string/upper-case (last row))
                    "X" (case (clojure.string/upper-case (first row))
                          "A" (+ 0 3) ; Rock beats scissors
                          "B" (+ 0 1) ; Paper beats rock
                          "C" (+ 0 2)) ; Scissors beats paper
                    "Y" (case (clojure.string/upper-case (first row))
                          "A" (+ 3 1) ; Rock draws rock
                          "B" (+ 3 2) ; Paper draws paper
                          "C" (+ 3 3)) ; Scissors draws scissors
                    "Z" (case (clojure.string/upper-case (first row))
                          "A" (+ 6 2) ; Rock loses to paper
                          "B" (+ 6 3) ; Paper loses to scissors
                          "C" (+ 6 1)) ; Scissors loses to rock
                    :error)]
    rps-score))


(time
 (let [a (slurp "2.txt")
       b (clojure.string/split a #"\n")]
   (println (apply + (map row-score b)))
   (println (apply + (map b-row-score b)))))

