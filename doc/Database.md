<h1>Database Structure</h1>
You have to create a Database with the following tables to start the server. 
Please check if an error occurs that you spelled everything right before you contact support.
<h2>1. Account Table</h2>
Name: accounts

<h3>Rows:</h3>

<table>
<tr>
<td>Name</td>
<td>Type</td>
<td>Extra</td>
</tr>
<tr>
<td>id</td>
<td>int</td>
<td>PRIMARY, AUTO INCREMENT</td>
</tr>
<tr>
<td>email</td>
<td>varchar(320)</td>
<td></td>
</tr>
<tr>
<td>pwhash</td>
<td>varchar(70)</td>
<td></td>
</tr>
<tr>
<td>username</td>
<td>varchar(20)</td>
<td></td>
</tr>
<tr>
<td>2fasecret</td>
<td>varchar(16)</td>
<td></td>
</tr>
<tr>
<td>role</td>
<td>varchar(20)</td>
<td></td>
</tr>
<tr>
<td>emailcode</td>
<td>varchar(6)</td>
<td></td>
</tr>
</table>

<h2>2. Currency Table</h2>
Name: currencies

<h3>Rows:</h3>

<table>
<tr>
<td>Name</td>
<td>Type</td>
<td>Extra</td>
</tr>
<tr>
<td>id</td>
<td>int</td>
<td>PRIMARY, AUTO INCREMENT</td>
</tr>
<tr>
<td>name</td>
<td>varchar(30)</td>
<td></td>
</tr>
<tr>
<td>short</td>
<td>varchar(10)</td>
<td></td>
</tr>
<tr>
<td>explorer_link</td>
<td>text</td>
<td></td>
</tr>
</table>

<h2>3. Wallet Table</h2>
Name: wallets

<h3>Rows:</h3>

<table>
<tr>
<td>Name</td>
<td>Type</td>
<td>Extra</td>
</tr>
<tr>
<td>id</td>
<td>int</td>
<td>PRIMARY, AUTO INCREMENT</td>
</tr>
<tr>
<td>SHORT OF CURRENCY</td>
<td>varchar(ADDRESS LENGTH)</td>
<td>Infinitely expandable</td>
</tr>
</table>

<h2>4. Faucet Table</h2>
Name: faucets

<h3>Rows:</h3>

<table>
<tr>
<td>Name</td>
<td>Type</td>
<td>Extra</td>
</tr>
<tr>
<td>id</td>
<td>int</td>
<td>PRIMARY, AUTO INCREMENT</td>
</tr>
<tr>
<td>name</td>
<td>varchar(50)</td>
<td></td>
</tr>
<tr>
<td>shortdesc</td>
<td>varchar(300)</td>
<td></td>
</tr>
<tr>
<td>payoutrate</td>
<td>varchar(10)</td>
<td></td>
</tr>
<tr>
<td>currency</td>
<td>varchar(10)</td>
<td></td>
</tr>
<tr>
<td>link</td>
<td>varchar(200)</td>
<td></td>
</tr>
</table>

<h2>5. Newsletter Table</h2>
Name: newsletter

<h3>Rows:</h3>

<table>
<tr>
<td>Name</td>
<td>Type</td>
<td>Extra</td>
</tr>
<tr>
<td>id</td>
<td>int</td>
<td>PRIMARY, AUTO INCREMENT</td>
</tr>
<tr>
<td>email</td>
<td>varchar(320)</td>
<td></td>
</tr>
</table>