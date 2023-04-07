function getCookie(name) {
  if (!document.cookie) {
    return null;
  }

  const xsrfCookies = document.cookie.split(';')
    .map(c => c.trim())
    .filter(c => c.startsWith(name + '='));

  if (xsrfCookies.length === 0) {
    return null;
  }
  return decodeURIComponent(xsrfCookies[0].split('=')[1]);
}

$('#submit-button').click(function() {
    const csrfToken = getCookie('CSRF-TOKEN');
    const headers = new Headers({
        'Content-Type': 'application/json',
        'X-CSRF-TOKEN': csrfToken
    });
    fetch('http://localhost:8080/paste', {
        method: 'POST',
        headers,
        credentials: 'include',
        body: JSON.stringify({
            title: document.getElementById('title-input').value,
            text: document.getElementById('text-input').value,
            lang: document.getElementById('lang-select').value
        })
    }).then(response => {
        return response.json();
    }).then(json => {
        window.location.href = `/paste/${json.id}`;
    });
});

// handle login form animation
$('.message a').click(function(){
   $('form').animate({height: "toggle", opacity: "toggle"}, "slow");
});

// copy and share button on paste page
$('#btn-copy').click(function() {
    let copyText = document.getElementById('paste').innerText;
    navigator.clipboard.writeText(copyText);
});

$('#btn-share').click(function() {
    navigator.clipboard.writeText(window.location.href);
});

// counter on home page
$('.counter-count').each(function () {
    $(this).prop('Counter',0).animate({
        Counter: $(this).text()
    },
    {
        duration: 4000,
        easing: 'swing',
        step: function (now) {
            $(this).text(Math.ceil(now));
        }
    });
});