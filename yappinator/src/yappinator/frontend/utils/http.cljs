(ns yappinator.frontend.utils.http
  (:require [ajax.core :as ajax]))

(def json-request-format (ajax/json-request-format))
(def json-response-format (ajax/json-response-format {:keywords? true}))
