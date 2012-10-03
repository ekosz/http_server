window.generateBoard = function() {
  var array = [];
  for(i=0; i < 9; i++) {
    var text = $(".cell"+(i+1)+" a").text();
    array[i] = $.inArray(text, ["x", "o"]) >= 0 ? text : null;
  }
  return JSON.stringify(array);
}

$(function() {
  $(".cell a").click(function() { 
    var cellNum = $(this).parent().attr("class").substring(9);
    $("form").find("input[name='move']").val(cellNum);
    $("form").find("input[name='board']").val(generateBoard());
    $("form").submit();
    return false;
  });
});
