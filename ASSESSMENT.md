# Android Developer Challenge
**Company**: nc43|tech
**Maximum amount of time allowed**: 5 days.

## Task
You are requested to develop an Android app, using Kotlin, for a taproom bar to display their
beers catalog. The app consists on two screens.
The first screen shows a list of beers that you will retrieve from [PUNK API](https://punkapi.com).
- It is enough to display the first 100 elements
- Every item must contain the name of the beer and the tagline, and its background
must be white if the beer is available or gray otherwise.
- When an item is pressed, the app navigates to a second screen.

The second screen offers a detailed view of the item, showcasing the following information:
- Name
- Description
- Image
- Alcohol by volume (ABV)
- Bitterness (IBU)
- Food pairing
- Button to notify the barrel is empty and needs replacement. Clicking this button will
toggle the availability of the product.
- Text stating that there’s no availability of this product. This text is shown only if the
product is not available.
The app must keep track of the beers marked as not available even if the app is closed.
When a beer is marked as not available and the user goes back to the screen with the list,
this item must be shown as not available.

What we look at
- Your code is tested, specially the most important parts of the app.
- We are interested in how you structure your code so that’s is easily extendable,
complies with the best OO practices, and it is easy to understand and modify.
