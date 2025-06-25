Get-ChildItem "Y:\Gerenciamento" -Recurse | ForEach-Object {
    $_.IsReadOnly = $false
    $_.Attributes = 'Normal'
}