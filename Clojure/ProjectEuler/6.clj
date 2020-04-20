(prn
 (time
  (- (#(* % %) (reduce + (range 1 101)))
     (reduce + (map #(* % %) (range 1 101))))))
