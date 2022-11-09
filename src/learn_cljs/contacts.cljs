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

(defn set-app-html! [html-str]
  (set! (.-innerHTML app-container) html-str))

(defn format-name [contact]
  (->> contact
       ((juxt :first-name :last-name))
       (str/join " ")))

(defn delete-icon [idx]
  [:span {:class "delete-icon"
          :data-idx idx}
   [:span {:class "mu mu-delete"}]])

(defn render-contact-list-item [idx contact selected?]
  [:div {:class (str "card contact-summary" (when selected? " selected"))
         :data-idx idx}
   [:div {:class "card-content"}
    [:div {:class "level"}
     [:div {:class "level-left"}
      [:div {:class "level-item"}
       (delete-icon idx)
       (format-name contact)]]
     [:div {:class "level-right"}
      [:span {:class "mu mu-right"}]]]]])

(defn render-contact-list [state]
  (let [{:keys [:contacts :selected]} state]
    [:div {:class "contact-list column is-4 hero is-fullheight"}
     (map-indexed (fn [idx contact]
                    (render-contact-list-item idx contact (= idx selected)))
                  contacts)]))

(defn on-open-contact [e state]
  (refresh!
   (let [idx (int (.. e -currentTarget -dataset -idx))]
     (assoc state :selected? idx
                  :editing? true))))

(defn attach-event-handlers! [state]
  (doseq [elem (array-seq (gdom/getElementByClass "contact-summary"))]
    (gevents/listen elem "click"
                    (fn [e] (on-open-contact e state)))))

(defn render-app! [state]
  (set-app-html!
   (hiccups/html
    [:div {:class "app-main"}
     [:div {:class "navbar has-shadow"}
      [:div {:class "container"}
       [:div {:class "navbar-brand"}
        [:span {:class "navbar-item"}
         "ClojureScript Contacts"]]]]
     [:div {:class "columns"}
      (render-contact-list state)
      [:div {:class "contact-details column is-8"}
       (section-header (:editing? state))
       [:div {:class "hero is-fullheight"}
        (if (:editing? state)
          (render-contact-details (get-in state [:contacts (:selected state)] {}))
          [:p {:class "notice"} "No contact selected"])]]]])))

(defn form-field
  ([id value label] (form-field id value label "text"))
  ([id value label type]
   [:div {:class "field"}
    [:label {:class "label"} label]
    [:div {:class "control"}
     [:input {:id id
              :value value
              :type type
              :class "input"}]]]))

(defn render-contact-details [contact]
  (let [address (get contact :address {})]
    [:div {:id "contact-form" :class "contact-form"}
     (form-field "input-first-name" (:first-name contact) "First Name")
     (form-field "input-last-name" (:last-name contact) "Last Name")
     (form-field "input-email" (:email contact) "Email" "email")
     [:fieldset
      [:legend "Address"]
      (form-field "input-street" (:street address) "Street")
      (form-field "input-city" (:city address) "City")
      (form-field "input-state" (:state address) "State")
      (form-field "input-postal" (:postal address) "Postal Code")
      (form-field "input-country" (:country address) "Country")]]))

(defn refresh! [state]
  (render-app! state)
  (attach-event-handlers! state))

(refresh! initial-state)
