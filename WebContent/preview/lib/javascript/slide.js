// JavaScript Document
(function(a) {
    a.fn.jdMarquee = function(h, b) {
        if (typeof h == "function") {
            b = h;
            h = {};
        }
        var j = a.extend({
            deriction: "up",
            speed: 10,
            auto: false,
            width: null,
            height: null,
            step: 1,
            control: false,
            _front: null,
            _back: null,
            _stop: null,
            _continue: null,
            wrapstyle: "",
            stay: 5000,
            delay: 20,
            dom: "div>ul>li".split(">"),
            mainTimer: null,
            subTimer: null,
            tag: false,
            convert: false,
            btn: null,
            disabled: "disabled",
            pos: {
                ojbect: null,
                clone: null
            }
        },
        h || {});
        var u = this.find(j.dom[1]);
        var e = this.find(j.dom[2]);
        var r;
        if (j.deriction == "up" || j.deriction == "down") {
            var l = u.eq(0).outerHeight();
            var c = j.step * e.eq(0).outerHeight();
            u.css({
                width: j.width + "px",
                overflow: "hidden"
            });
        }
        if (j.deriction == "left" || j.deriction == "right") {
            var n = e.length * e.eq(0).outerWidth();
            u.css({
                width: n + "px",
                overflow: "hidden"
            });
            var c = j.step * e.eq(0).outerWidth();
        }
        var o = function() {
            var s = "<div style='position:relative;overflow:hidden;z-index:1;width:" + j.width + "px;height:" + j.height + "px;" + j.wrapstyle + "'></div>";
            u.css({
                position: "absolute",
                left: 0,
                top: 0
            }).wrap(s);
            j.pos.object = 0;
            r = u.clone();
            u.after(r);
            switch (j.deriction) {
            default:
            case "up":
                u.css({
                    marginLeft:
                    0,
                    marginTop: 0
                });
                r.css({
                    marginLeft: 0,
                    marginTop: l + "px"
                });
                j.pos.clone = l;
                break;
            case "down":
                u.css({
                    marginLeft:
                    0,
                    marginTop: 0
                });
                r.css({
                    marginLeft: 0,
                    marginTop: -l + "px"
                });
                j.pos.clone = -l;
                break;
            case "left":
                u.css({
                    marginTop:
                    0,
                    marginLeft: 0
                });
                r.css({
                    marginTop: 0,
                    marginLeft: n + "px"
                });
                j.pos.clone = n;
                break;
            case "right":
                u.css({
                    marginTop:
                    0,
                    marginLeft: 0
                });
                r.css({
                    marginTop: 0,
                    marginLeft: -n + "px"
                });
                j.pos.clone = -n;
                break;
            }
            if (j.auto) {
                k();
                u.hover(function() {
                    m(j.mainTimer);
                },
                function() {
                    k();
                });
                r.hover(function() {
                    m(j.mainTimer);
                },
                function() {
                    k();
                });
            }
            if (b) {
                b();
            }
            if (j.control) {
                g();
            }
        };
        var k = function(s) {
            m(j.mainTimer);
            j.stay = s ? s: j.stay;
            j.mainTimer = setInterval(function() {
                t();
            },
            j.stay);
        };
        var t = function() {
            m(j.subTimer);
            j.subTimer = setInterval(function() {
                q();
            },
            j.delay);
        };
        var m = function(s) {
            if (s != null) {
                clearInterval(s);
            }
        };
        var p = function(s) {
            if (s) {
                a(j._front).unbind("click");
                a(j._back).unbind("click");
                a(j._stop).unbind("click");
                a(j._continue).unbind("click");
            } else {
                g();
            }
        };
        var g = function() {
            if (j._front != null) {
                a(j._front).click(function() {
                    a(j._front).addClass(j.disabled);
                    p(true);
                    m(j.mainTimer);
                    j.convert = true;
                    j.btn = "front";
                    t();
                    if (!j.auto) {
                        j.tag = true;
                    }
                    f();
                });
            }
            if (j._back != null) {
                a(j._back).click(function() {
                    a(j._back).addClass(j.disabled);
                    p(true);
                    m(j.mainTimer);
                    j.convert = true;
                    j.btn = "back";
                    t();
                    if (!j.auto) {
                        j.tag = true;
                    }
                    f();
                });
            }
            if (j._stop != null) {
                a(j._stop).click(function() {
                    m(j.mainTimer);
                });
            }
            if (j._continue != null) {
                a(j._continue).click(function() {
                    k();
                });
            }
        };
        var f = function() {
            if (j.tag && j.convert) {
                j.convert = false;
                if (j.btn == "front") {
                    if (j.deriction == "down") {
                        j.deriction = "up";
                    }
                    if (j.deriction == "right") {
                        j.deriction = "left";
                    }
                }
                if (j.btn == "back") {
                    if (j.deriction == "up") {
                        j.deriction = "down";
                    }
                    if (j.deriction == "left") {
                        j.deriction = "right";
                    }
                }
                if (j.auto) {
                    k();
                } else {
                    k(4 * j.delay);
                }
            }
        };
        var d = function(w, v, s) {
            if (s) {
                m(j.subTimer);
                j.pos.object = w;
                j.pos.clone = v;
                j.tag = true;
            } else {
                j.tag = false;
            }
            if (j.tag) {
                if (j.convert) {
                    f();
                } else {
                    if (!j.auto) {
                        m(j.mainTimer);
                    }
                }
            }
            if (j.deriction == "up" || j.deriction == "down") {
                u.css({
                    marginTop: w + "px"
                });
                r.css({
                    marginTop: v + "px"
                });
            }
            if (j.deriction == "left" || j.deriction == "right") {
                u.css({
                    marginLeft: w + "px"
                });
                r.css({
                    marginLeft: v + "px"
                });
            }
        };
        var q = function() {
            var v = (j.deriction == "up" || j.deriction == "down") ? parseInt(u.get(0).style.marginTop) : parseInt(u.get(0).style.marginLeft);
            var w = (j.deriction == "up" || j.deriction == "down") ? parseInt(r.get(0).style.marginTop) : parseInt(r.get(0).style.marginLeft);
            var x = Math.max(Math.abs(v - j.pos.object), Math.abs(w - j.pos.clone));
            var s = Math.ceil((c - x) / j.speed);
            switch (j.deriction) {
            case "up":
                if (x == c) {
                    d(v, w, true);
                    a(j._front).removeClass(j.disabled);
                    p(false);
                } else {
                    if (v <= -l) {
                        v = w + l;
                        j.pos.object = v;
                    }
                    if (w <= -l) {
                        w = v + l;
                        j.pos.clone = w;
                    }
                    d((v - s), (w - s));
                }
                break;
            case "down":
                if (x == c) {
                    d(v, w, true);
                    a(j._back).removeClass(j.disabled);
                    p(false);
                } else {
                    if (v >= l) {
                        v = w - l;
                        j.pos.object = v;
                    }
                    if (w >= l) {
                        w = v - l;
                        j.pos.clone = w;
                    }
                    d((v + s), (w + s));
                }
                break;
            case "left":
                if (x == c) {
                    d(v, w, true);
                    a(j._front).removeClass(j.disabled);
                    p(false);
                } else {
                    if (v <= -n) {
                        v = w + n;
                        j.pos.object = v;
                    }
                    if (w <= -n) {
                        w = v + n;
                        j.pos.clone = w;
                    }
                    d((v - s), (w - s));
                }
                break;
            case "right":
                if (x == c) {
                    d(v, w, true);
                    a(j._back).removeClass(j.disabled);
                    p(false);
                } else {
                    if (v >= n) {
                        v = w - n;
                        j.pos.object = v;
                    }
                    if (w >= n) {
                        w = v - n;
                        j.pos.clone = w;
                    }
                    d((v + s), (w + s));
                }
                break;
            }
        };
        if (j.deriction == "up" || j.deriction == "down") {
            if (l >= j.height && l >= j.step) {
                o();
            }
        }
        if (j.deriction == "left" || j.deriction == "right") {
            if (n >= j.width && n >= j.step) {
                o();
            }
        }
    };
})(jQuery);