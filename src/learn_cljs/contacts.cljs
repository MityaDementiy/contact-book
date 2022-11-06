(ns learn-cljs.contacts
  (:require-macros [hiccups.core :as hiccups])
  (:require [hiccups.runtime]
            [goog.dom :as gdom]
            [goog.events :as gevents]
            [clojure.string :as str]))

;; Models and State

(def contact-list [])

(defn make-address [address]
  (select-keys address [:street :city :state :country :postal]))

(defn maybe-set-address [contact]
  (if (:address contact)
    (update contact :address make-address)
    contact))

(defn make-contact [contact]
  (-> contact
      (select-keys [:first-name :last-name :email :address])
      (maybe-set-address)))

(defn add-contact [contact-list input]
  (conj contact-list
        (make-contact input)))

(defn remove-contact [contact-list idx]
  (vec
    (concat
      (subvec contact-list 0 idx)
      (subvec contact-list (inc idx)))))

(defn replace-contact [contact-list idx input]
  (assoc contact-list idx (make-contact input)))

(def initial-state
  {:contacts contact-list
   :selected nil
   :editing false})

;; UI

(def app-container (gdom/getElement "app"))

(defn attach-event-handlers! [state])

(defn set-app-html! [html-str]
  (set! (.-innerHTML app-container) html-str))

(defn render-app! [state]
  (set-app-html! (hiccups/html [:div "Hello"])))

(defn refresh! [state]
  (render-app! state)
  (attach-event-handlers! state))

(refresh! initial-state)

(defn format-name [contact]
  (->> contact
       ((juxt :first-name :last-name))
       (str/join " ")))
