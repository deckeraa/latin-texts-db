# latin-texts-db

## Prerequisites

You will need [Leiningen][] 2.0.0 or above installed.

[leiningen]: https://github.com/technomancy/leiningen

## Install
You will need a database file with the tables and some wordforms to get you starting. Go to `./resources/db/` and run `fetch_db.sh`. This will download a file called `latin_shareable.db` and should rename it to `latin.db`. If it doesn't rename it, do so manually.

Next run `npm i` to install node packages.

Then, in separate terminals, run the following processes:
- `lein ring server`
- `npx shadow-cljs watch app`
This will start the web server and the js compile, respectively.

Once the js compile is complete, you should be able to refresh the browser page at http://localhost:3000/.

## Insert your first text.
Click the "Insert Text" button and use the UI to insert your first Latin text. Then click the "Text" button to get to the text viewer.

## License

Copyright © 2026 Aaron Decker
