(ns ^:figwheel-hooks learn-cljs.contacts
  (:require
   [goog.dom :as gdom]))



;; specify reload hook with ^:after-load metadata
(defn ^:after-load on-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)
