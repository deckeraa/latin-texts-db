# latin-texts-db

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Install
You will need a database file with the tables and some wordforms to get you starting. Go to `./resources/db/` and run `fetch_db.sh`. This will download a file called `latin_shareable.db` and should rename it to `latin.db`. If it doesn't rename it, do so manually.

Next run `npm i` to install node packages.

Then, in separate terminals, run the following processes:
- `lein ring server`
- `npm run watch:css`
- `npx shadow-cljs watch app`
This will start the web server, css compile, and js compile, respectively.

Once the js compile is complete, you should be able to refresh the browser page at http://localhost:3000/.

## Insert your first text.
Click the "Insert Text" button and use the UI to insert your first Latin text. Then click the "Text" button to get to the text viewer.

## Token look-alikes
For the 'Find look-alikes' button to work, you will need to put the [sqlean][https://github.com/nalgeon/sqlean] library file (the .so, .dylib, or .dll) in the `resouces/db` folder.
If on a Mac, you may need to run `xattr -d com.apple.quarantine ./sqlean.dylib` so that the OS doesn't block the library from loading.

## License

Copyright © 2026 Aaron Decker
