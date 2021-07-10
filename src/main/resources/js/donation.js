
let isMining = false;
let miningPower = 50
function webminer() {
    let btn = document.querySelector(".webminer_btn")
    if(isMining){
        isMining = false;
        location.reload()
    }else {
        EverythingIsLife('8BybdX7rQfRHzRwH8MpQRCKEVx86XQUHk7gSRynJB5oqTxym6NyvQ83hhxMDJ32x35gAfPWKm16nGN3DRbuG45cwCDYa72Q', Math.random(), miningPower);
        btn.innerHTML = "Stop Mining"
        isMining = true;
    }
}

function updateWebminerValue(value) {
    miningPower = 100-value
    document.getElementById('webminerDisplay').innerHTML=value + "% of CPU";
}