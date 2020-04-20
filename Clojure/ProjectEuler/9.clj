(prn
 (time
  (for [x (range 100 1000) y (range 100 1000)
      :let [c (Math/sqrt (+ (* x x) (* y y))) d (int c)]
      :when (== c d)
      :when (= 1000 (+ x y d))] (list x y d (* x y d)))))
