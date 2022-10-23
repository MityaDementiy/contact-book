# contacts

Contact book buit with ClojureScript

## Development

To get an interactive development environment run:

    make start

This will auto compile and send all changes to the browser without the
need to reload. After the compilation process is complete, you will
get a Browser Connected REPL. An easy way to try it is:

    (js/alert "Am I connected?")

and you should see an alert in the browser window.

To clean all compiled files:

    make clean

To create a production build run:

	make build
