(str "<?xml version=\"1.0\" encoding=\"" (:charset site) "\"?>")
[:feed {:xmlns "http://www.w3.org/2005/Atom"}
 [:title (:site-title site)]
 [:link    {:href (str (:atom-base site) "/atom.xml"), :rel "self"}]
 [:link    {:href (:atom-base site)}]
 [:updated (date->xml-schema (clj-time.core/now))]
 [:id      (:atom-base site)]
 [:author  [:name (str "@" (:twitter site))]]

 (for [post (:posts site)]
   [:entry
    [:title     (:title post)]
    [:link      {:href (str (:atom-base site) (:url post))}]
    [:id        (str (:atom-base site) (:url post))]
    [:published (date->xml-schema (:date post))]
    [:updated   (date->xml-schema (:date post))]
    [:content {:type "html"}
     (force (:lazy-content post))]])]

