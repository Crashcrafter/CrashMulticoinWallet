function addCurrencyboxEvents(){
	Array.prototype.forEach.call(document.getElementsByClassName("currencybox"), function (e){
		e.addEventListener("click", function (event){
			let short = event.currentTarget.className.replace(" currencybox", "")
			coininfoload(short)
		})
	})
}

//TODO: Remove JQuery
function coininfoload(short) {
	let xhttp = new XMLHttpRequest()
	xhttp.onreadystatechange = function (){
		if(this.readyState === 4 && this.status === 200){
			document.querySelector(".backup").innerHTML = document.querySelector(".currenciesbox").innerHTML
			document.querySelector(".currenciesbox").innerHTML = this.responseText
			document.documentElement.scrollTop = 0
			addCurrencyboxEvents()
		}
	}
	xhttp.open("GET", "/wallet/info?id=" + short, true)
	xhttp.send()
}

function copyAddress() {
	navigator.clipboard.writeText(document.querySelector(".hidden_address").value).then(() => {})
	document.querySelector(".address button").innerHTML = "Copied!"
}

let interval = null
loadListing()

function loadListing(){
	clearInterval(interval)
	if(document.querySelector(".refresh-button") !== null)
	document.querySelector(".refresh-button").classList.add("hidden")
	let xhttp = new XMLHttpRequest()
	xhttp.onreadystatechange = function (){
		if(this.readyState === 4 && this.status === 200){
			if(this.responseText.length <= 10){
				loadListing()
				return
			}
			document.querySelector(".currenciesbox").innerHTML = this.responseText
			addCurrencyboxEvents()
			updateValues()
			interval = window.setInterval(function(){
				updateValues()
			}, 30000)
		}
	}
	xhttp.open("GET", "/wallet?getListing", true)
	xhttp.send()
}

function backtoListing(){
	document.querySelector(".currenciesbox").innerHTML = document.querySelector(".backup").innerHTML
	addCurrencyboxEvents()
}

function updateValues(){
	let xhttp = new XMLHttpRequest()
	xhttp.onreadystatechange = function (){
		if(this.readyState === 4 && this.status === 200){
			let jsonresponsemap = JSON.parse(this.responseText)
			Object.keys(jsonresponsemap).forEach(function (item){
				try{
					let obj = jsonresponsemap[item]
					document.querySelector(".balance-" + item).innerHTML = obj.bal + " " + item.toUpperCase()
					document.querySelector(".value-" + item).innerHTML = obj.value + " USD"
					document.querySelector(".portpercent-" + item).innerHTML = obj.portpercent + " %"
					document.querySelector(".marketcap-" + item).innerHTML = obj.marketcap + " USD"
					document.querySelector(".dayvolume-" + item).innerHTML = obj.dayvolume + " USD"
					let daychangeobj = document.querySelector(".daychange-" + item)
					daychangeobj.classList.add("down")
					daychangeobj.classList.remove("up")
					var prefix = ""
					if(obj.daychange > 0) {
						daychangeobj.classList.remove("down")
						daychangeobj.classList.add("up")
						prefix = "+"
					}
					daychangeobj.innerHTML = prefix + obj.daychange + " %"
					document.querySelector(".currentprice-" + item).innerHTML = obj.currentprice + " USD"
					const async = window.setTimeout(function (){
						document.querySelector(".refresh-button").classList.remove("hidden")
						clearInterval(async)
					}, 5000)
				}catch (e){}
			})
		}
	}
	xhttp.open("GET", "/wallet?getBal", true)
	xhttp.send()
}
