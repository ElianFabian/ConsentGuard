# Languages

ConsentGuard matches the wording of banners in all the languages below at the same time. It does not try to guess the language of a page. This matters because a page translated by Google Translate, by Firefox Translations or by another tool shows text in a language the site never wrote, and the text can change a moment after the page loads.

## What is included

| Language | Code |
| --- | --- |
| English | en | 
| German | de |
| French | fr | 
| Spanish | es | 
| Italian | it | 
| Dutch | nl | 
| Portuguese | pt | 
| Kiswahili | sw | 
| Afrikaans | af | 
| Arabic | ar | 
| Zulu | zu | 
| Xhosa | xh | 
| Hausa | ha | 
| Somali | so | 
| Yoruba | yo | 
| Igbo | ig | 
| Amharic | am | 

The phrases were compared with the labels that consent tools (OneTrust, Usercentrics, Didomi, Sourcepoint, Cookiebot and the Google consent form many apps show) use in that language. It does not mean a native speaker has read every phrase. It does not make it press the wrong button, because every pack is checked against the accept guard lists of all the other packs (see below).

## What changed in 0.0.2

- The Google consent form that many Android apps show ("Consent", "Do not consent", "Manage options", "Confirm choices") is now recognised in English, German, French, Spanish, Italian, Dutch and Portuguese. Before, the German and Dutch versions, and the Italian one with its "Acconsenti" and "Nega il consenso" buttons, were not recognised as banners at all.

- More ways of saying no: "Continue without consent" and its German, French and Italian forms, "Deselect all", "Close and reject", "No cookies, thanks", "Rather not", "Object to legitimate interest", and the "Not now" style dismissals as a last resort.

- More accept and pay wording that must never be tapped, such as "Continue with recommended cookies", "Allow and continue", "Continue with ads", "Use for free" and the German "Mit Werbung lesen" walls with contentpass or freechoice.

- The IAB TCF 2.2 purpose names ("Use limited data to select advertising", "Measure content performance" and the rest) count as consent panel wording, so preference panels are recognised more reliably.

- Withdraw and revoke buttons ("Withdraw consent", "Einwilligung widerrufen", "Retirer mon consentement" and so on) are no longer treated as reject buttons. They belong to privacy centres and account pages a person opened on purpose.
- "Block all cookies" and "Block third party cookies" are no longer reject wording. A browser's own cookie settings use them, and pressing them there would sign the person out of every site.

- Refusal and necessary only wording was compared with the button patterns DuckDuckGo's autoconsent uses, and the gaps were filled in English, German, French, Spanish, Italian, Dutch and Portuguese ("Weiger alle", "Je n'accepte rien", "Ohne Akzeptieren weiter", "Solo cookies técnicas", "Seguir sem aceitar" and more).

- Dutch refusals such as "Geen cookies toestaan" and "Ik accepteer geen cookies" were read as accept buttons, because "cookies toestaan" and "ik accepteer" matched anywhere in a label. Those accept phrases now have to be the whole label.

- One or two letter fallback words in Hausa, Igbo and Yoruba are now exact phrases. As plain phrases they matched inside ordinary words, "ki" inside "cookies" for example.

## How it stays safe across languages

- Accent marks are folded away before matching, so "Paramétrer" and "Parametrer" match the same phrase.

- Short or ambiguous phrases are written as exact phrases (marked with `=` in the pack files), so "no acepto" is never mistaken for "acepto".

- `kotlinc -script tools/check_languages.kts` fails if any label that should accept could also be read as a reject, necessary only, manage or confirm phrase in any language, or the other way round. It also fails on a plain phrase shorter than three letters, and on a generated phrase that is not folded the way the app folds text.

- If a screen looks like a sign in, payment or bank page, ConsentGuard stays out of it, in every language.

