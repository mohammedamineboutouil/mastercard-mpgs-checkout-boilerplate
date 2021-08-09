import React, {useEffect, useState} from "react";
import axios from "axios";
import "./App.css";

const {REACT_APP_MPGS_MERCHANT_ID, REACT_APP_MPGS_BASE_URL, REACT_APP_SERVER_URL} = process.env;

const MASTER_CARD_CHECKOUT_JS_SRC = `${REACT_APP_MPGS_BASE_URL}/checkout/version/61/checkout.js`;
const MPGS_TIMEOUT = 5000;

const onScriptLoad = ({initialized, sessionId}) => {
    const {Checkout} = window;

    if (!Checkout) {
        return;
    }

    Checkout.configure({
        session: {
            id: sessionId,
        },
        merchant: REACT_APP_MPGS_MERCHANT_ID,
        interaction: {
            merchant: {
                name: "Test merchant",
                address: {
                    line1: "1, test lane",
                    line2: "Victoria Island",
                },
                email: "support@test.com",
                phone: "+2348126837629",
            },
            locale: 'en_US',
            displayControl: {
                billingAddress: 'HIDE',
                paymentConfirmation: 'HIDE',
                orderSummary: 'HIDE',
                shipping: 'HIDE'
            }
        }
    });
    initialized();
};

const loadCallbacks = () => {
    const script = document.createElement("script");
    const code = `
            function cancelCallback() {}
            function timeoutCallback() {}
            function errorCallback(error) {}
            function completeCallback(resultIndicator, sessionVersion) {}
        `;
    script.type = "text/javascript";
    script.async = true;
    try {
        script.appendChild(document.createTextNode(code));
        document.body.appendChild(script);
    } catch (e) {
        script.text = code;
        document.body.appendChild(script);
    }
};

const loadScript = async () => {
    if (!document) {
        return Promise.reject();
    }

    return new Promise((resolve, reject) => {
        setTimeout(() => {
            reject();
        }, MPGS_TIMEOUT);

        const prevScript = document.querySelector(
            `script[src="${MASTER_CARD_CHECKOUT_JS_SRC}"]`
        );

        if (prevScript) {
            prevScript.remove();
        }

        loadCallbacks();

        const script = document.createElement("script");
        script.src = MASTER_CARD_CHECKOUT_JS_SRC;

        script.async = true;
        script.onerror = reject;

        script["data-error"] = "errorCallback";
        script["data-cancel"] = "cancelCallback";
        script["data-complete"] = "completeCallback";
        script["data-timeout"] = "timeoutCallback";

        script.onload = async () => {
            await axios
                .post(`${REACT_APP_SERVER_URL}/payment/checkout`, {cost: 20})
                .then(async (res) => {
                    const {
                        session: {id},
                    } = res.data;
                    await onScriptLoad({
                        initialized: resolve,
                        sessionId: id,
                    });
                })
                .catch((reason) => {
                    console.error(reason);
                });
        };
        document.body.appendChild(script);
    });
};

const App = () => {
    const [initializing, setInitializing] = useState(false);
    const [error, setError] = useState();

    useEffect(() => {
        setInitializing(true);
        loadScript()
            .then(() => setInitializing(false))
            .catch(() => console.warn("CANT NOT LOAD MASTERCARD PAYMENT"));
    }, []);

    (() => {
        window.errorCallback = (err) => {
            console.log(JSON.stringify(err));
            if (err) setError(err);
        };
        window.cancelCallback = () => {
            console.log("Payment cancelled");
        };
        window.timeoutCallback = () => {
            console.log("Payment timeout");
        };
        window.completeCallback = (resultIndicator, sessionVersion) => {
            console.log(resultIndicator, sessionVersion);
        };
    })();

    const pay = () => {
        const {Checkout} = window;
        if (!Checkout) {
            return;
        }
        Checkout.showLightbox();
    };

    return (
        <div className="App">
            {initializing && <div>Loading...</div>}
            {!initializing && <button onClick={pay}>PAY</button>}
            {error && <div>Error: <ul>
                <li>Cause: {error.cause}</li>
                <li>Explanation: {error.explanation}</li>
            </ul>
            </div>
            }
        </div>
    );
};

export default App;
