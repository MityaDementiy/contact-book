start:
	clj -A:fig:build

clean:
	rm -rf target/public

build: clean
	clojure -A:fig:min