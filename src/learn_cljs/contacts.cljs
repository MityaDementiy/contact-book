(ns ^:figwheel-hooks learn-cljs.contacts
  (:require
   [goog.dom :as gdom]))

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
