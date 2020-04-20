(require '[clojure.xml :as xml])
(require '[clojure.string :as str])

(defn parse
  [s]
  (xml/parse
   (java.io.ByteArrayInputStream. (.getBytes s))))

;; This function highlights pre-processing arguments before passing them to the n-arity function

(def getPanDate
  "Returns panchang for today as PersistentVector. Can also take dd or dd mm or dd mm yyyy as arguments"
  (let [dt (str/split (.format (new java.text.SimpleDateFormat "yyyy-MM-dd")
                                          (java.util.Date.))
                                 #"-")
        dd (Integer/parseInt (nth dt 2))
        mm (Integer/parseInt (nth dt 1))
        yy (Integer/parseInt (nth dt 0))
        url "http://www.mypanchang.com/compactfeed.php?mode=0"
        city "Vancouver-BC-Canada"]
    (fn
      ([] (getPanDate dd mm yy))
      ([d] (getPanDate d mm yy))
      ([d m] (getPanDate d m yy))
      ([d m y]
       {:pre [(and (pos-int? d) (<= d 31)), (and (pos-int? m) (<= m 12)), (and (pos-int? y) (<= y yy))]}
       (let [query (str url"&cityname="city"&yr="y"&mn="m"&dt="d)
             pxml (slurp query)
             content (parse pxml)
             out1 (first (:content (nth (:content (nth  (:content (first (:content content))) 4)) 3)))
             out2 (str/split out1 #"<br/>")]
         (prn "URL: " query) (prn "City:" city) (take (dec (count out2)) out2))))))

(doseq [i (getPanDate)]
  (prn i))
