# Best effort statement

CookieRejecter works on a best effort basis.

## What CookieRejecter tries to do

When it sees a supported cookie or tracking prompt, it shows a floating button. Tap it to reject optional choices. If the form needs a separate submission, the overlay asks you to confirm when it finishes checking the form.

## Languages

Several language packs are drafts that no native speaker has checked yet. See [languages](LANGUAGES.md). On those, CookieRejecter may miss a banner. A page translated by a browser can also change after CookieRejecter has looked, so it may need a second moment to settle.

## What it does not promise

- That every optional choice on every banner is switched off.

- That a site will stop tracking you, or will respect what was chosen.

- That using it meets the requirements of any privacy law, for you or for a site.

- That it works on every app or website, or keeps working when a site changes its banner.

## Why it can miss something

- Banners are built in many different ways and their wording and layout change without notice.

- Some sites draw their switches in a way Android cannot see, so CookieRejecter cannot read or change them.

- Some panels list hundreds of separate companies. CookieRejecter has limits on how far it scrolls and how often it taps, so a very long panel may not be checked completely.

- A slow page may show its options late, or change them after CookieRejecter has looked.

- A tap can fail to register, and on some banners CookieRejecter cannot see whether a switch really changed.

## What it does when it is not sure

- It does not press Save or Confirm if a switch that should be off is still on, or if the panel was too long to check completely. The panel is left as it is, so you can finish it yourself.

- It is built to skip buttons that read like accept all, pay, subscribe or sign in.

## Where it acts

CookieRejecter only acts on a screen that looks like a cookie or tracking consent banner: consent wording together with buttons a banner has. It does not act in its own app, in the phone settings, in permission and install dialogs, on the home screen, in the phone app or in the keyboard. It cannot be certain. A screen that looks enough like a banner could still be tapped by mistake.

## What it leaves on

Switches marked as required, essential, necessary, mandatory, always on, or similar are left on, because a site may not work without them. CookieRejecter cannot check that such a label is truthful. A label that says required but also names advertising or measurement is not trusted, and that switch is turned off. Switches that protect you, such as "do not sell my data", are also left on.

## What it reads and keeps

CookieRejecter reads screen text to find consent prompts and can inspect screenshots in memory to verify switch states on recognized Google forms. See [Privacy](PRIVACY.md) for what it stores and how you can verify its permissions.

## Your part

- For sites where it matters, check the cookie settings yourself.

- Report a banner it misses so it can be added.

- Turn the service off in the accessibility settings whenever you like.

## No warranty

CookieRejecter is provided as it is, under the MIT licence, without any warranty. See [LICENSE](LICENSE).
