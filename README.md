# Haaivin
A hyphenation library in Kotlin for Android. 

## Background
Android does not do hyphenation by default (starting with Android Q). Usually that means wrapping all of the word to the next line. 
However, when the word itself takes up more space than the layout it is in, it is by default wrapped wherever the text starts running out of bounds, without any hyphens to indicate the word is broken.
This can lead to some awkward breaks, ones you probably will no be aware of as a developer since either the layout width is dependent on screen width,
or the user chooses to use the Android accessibility feature to scale their text. An example of where this default wrapping can cause some red faces compared with how it should be done:

<img src="https://github.com/soldierinwhite/Haaivin/assets/8885532/c454f009-6ece-468b-95cd-aed858a677b3" alt="drawing" width="300"/>

To apply the correct hyphenation, you need to first add hyphenation to your app style or theme. An example for Material 3 in Compose:
```
val typography = Typography(
  ...
  bodyLarge = TextStyle(
    ...
    hyphens = Hyphens.Auto
  )
  ...
)
```
That gets you most of the way. Hyphenation is now done according to the language locale set for the app. If you 
[override the system default](https://developer.android.com/guide/topics/resources/app-languages#impl-overview) you can even force the hyphenation to follow the language in the app.

This only gets you so far. What if you display texts in different languages in the same app? What if you show them on the same screen even? In that case it becomes really cumbersome to mess with app locales
as activities are recreated every time you do so.

In that case you need to either have the hyphenation already inserted in the texts you display. If only static resources are necessary, you can add [soft hyphens](https://en.wikipedia.org/wiki/Soft_hyphen)
manually to the text. For this there are some online hyphenation tools like https://www.ushuaia.pl/hyphen/.

For dynamic texts you need this to be automated somehow. Best case scenario is that your back end integration can add soft hyphens to any text in its API. However, if this is not a possibility for whatever reason
and you need to do it locally in the app, here is a library for you. It is based off of https://github.com/wbsoft/hyphenator and all credit to [wbsoft](https://github.com/wbsoft) for his work against which I 
could test the output of this library.

 
## Add dependency
```css
implementation 'com.github.soldierinwhite:Haaivin:<version>'
```

## Add the relevant Hunspell hyphenation dictionary
Hyphenation dictionaries define the patterns relevant to hyphenation for each language. The dictionaries and algorithm used by this project is tested on the [dictionaries](https://github.com/LibreOffice/dictionaries/tree/master) that are available by different open source licenses
from the LibreOffice project.
