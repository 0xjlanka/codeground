(require '[clojure.xml :as xml])
(require '[clojure.string :as str])

(defn parse
  [s]
  (xml/parse
   (java.io.ByteArrayInputStream. (.getBytes s))))

(defn getPanDate1
  "Takes one argument (date) in the format YYYY-MM-DD and returns panchang for that date as a PersistentVector"
  [s]
  (let [dstr (str/split s #"-")
        dd (Integer/parseInt (nth dstr 2))
        mm (Integer/parseInt (nth dstr 1))
        yy (Integer/parseInt (nth dstr 0))
        url "http://www.mypanchang.com/compactfeed.php?mode=0"
        city "Vancouver-BC-Canada"
        query (str url"&cityname="city"&yr="yy"&mn="mm"&dt="dd)
        pxml (slurp query)
        content (parse pxml)
        out1 (first (:content (nth (:content (nth  (:content (first (:content content))) 4)) 3)))
        out2 (str/split out1 #"<br/>")]
    (take (dec (count out2)) out2)))


(let [dt (.format (new java.text.SimpleDateFormat "yyyy-MM-dd") (java.util.Date.))
      ydt (-> (java.time.LocalDate/parse dt)(.minusDays 1) str)
      tdt (-> (java.time.LocalDate/parse dt)(.plusDays 1) str)]
  (println "\nPanchang for Yesterday:")
  (prn "-----------------------")
  (doseq [i  (getPanDate1 ydt)]
    (prn i))
  (println "-----------------------\n")
  (prn "Panchang for Today:")
  (prn "-----------------------")
  (doseq [i  (getPanDate1 dt)]
    (prn i))
  (println "-----------------------\n")
  (prn "Panchang for Tomorrow:")
  (prn "-----------------------")
  (doseq [i  (getPanDate1 tdt)]
    (prn i)))

