start:
	clj -A:fig:start

clean:
	rm -rf target/public

build: clean
	clojure -A:fig:min
