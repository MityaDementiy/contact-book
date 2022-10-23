(ns ^:figwheel-hooks learn-cljs.contacts
  (:require
   [goog.dom :as gdom]))

(def contact-list [])
(defn make-contact [contact]
  (select-keys contact [:first-name :last-name :email :address]))



;; specify reload hook with ^:after-load metadata
(defn ^:after-load on-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
