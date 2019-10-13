use rand::Rng;

extern crate phf;

// build.rs produces a codegen.rs file defining WORDLIST:
// static WORDLIST: phf::Map<&str,&str>
include!(concat!(env!("OUT_DIR"), "/codegen.rs"));

pub fn get_word(roll: &str) -> Option<String> {
    WORDLIST.get(roll).map(|s| s.to_string())
}

pub fn roll() -> String {
    let mut rng = rand::thread_rng();
    let mut results = Vec::<String>::with_capacity(5);
    for _ in 0..5 {
        let r = rng.gen_range(1, 7);
        results.push(r.to_string())
    }
    let result = results.join("");
    result
}
